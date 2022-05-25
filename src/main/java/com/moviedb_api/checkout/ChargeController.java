package com.moviedb_api.checkout;

import com.moviedb_api.address.Address;
import com.moviedb_api.address.AddressRepository;
import com.moviedb_api.address.AddressRequest;
import com.moviedb_api.cart.Cart;
import com.moviedb_api.cart.CartRepository;
import com.moviedb_api.customer.Customer;
import com.moviedb_api.customer.CustomerRepository;
import com.moviedb_api.security.AuthenticationFacade;
import com.moviedb_api.security.JwtTokenUtil;
import com.moviedb_api.tax.TaxRate;
import com.moviedb_api.tax.TaxRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.net.ApiResource;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.google.gson.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/checkout")
public class ChargeController {
    @Autowired
    private JwtTokenUtil authenticationService;

    @Autowired
    private StripeService paymentsService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private TaxRepository taxRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> checkoutUser(@RequestHeader HttpHeaders headers) {

        Integer userId = authenticationFacade.getUserId();

        Customer customer = customerRepository.findById(userId).get();
        List<Address> addresses = customer.getAddresses();
        Integer primaryAddressId = customer.getPrimaryAddress();

        //if no primary address, set the first address as primary
        if(primaryAddressId == null || primaryAddressId == 0) {
            primaryAddressId = addresses.get(0).getId();
            customer.setPrimaryAddress(primaryAddressId);
            customerRepository.save(customer);
            System.out.println("Primary Address Set");
        }

        Iterable<Cart> cart = cartRepository.findAllByUserIdOrderByCreatedDateDesc(String.valueOf(userId));

        HashMap<String, Object> response = new HashMap<>();
        response.put("addresses", addresses);
        response.put("defaultId", primaryAddressId);

        Double subTotal = 0.0;
        for(Cart item: cart){
            subTotal += item.getQuantity() * item.getMovie().getPrice();
        }
        response.put("subTotal", subTotal);

        if(addresses.size() != 0) {
            Integer finalPrimaryAddressId = primaryAddressId;
            Address address = addresses.stream().filter(a -> a.getId() == finalPrimaryAddressId).collect(Collectors.toList()).get(0);
            Iterable<TaxRate> tax = taxRepository.findByCity(address.getCity());


            Double rate = 0.0775;
            for(TaxRate item: tax) {
                rate = item.getRate().doubleValue();
            }

            Double salesTax = subTotal * rate;
            Double total = salesTax + subTotal;

            response.put("salesTax", salesTax);
            response.put("total", total);
        }

        else {
            response.put("salesTax", 0.0);
            response.put("total", subTotal);
        }

        response.put("cart", cart);

        return new ResponseEntity<>(
                response,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<?> checkoutUserAddress(
            @RequestHeader HttpHeaders headers,
            @RequestBody AddressRequest request
    ) {
        HashMap<String, Object> response = new HashMap<>();
        Integer userId = authenticationFacade.getUserId();

        Customer customer = customerRepository.findById(userId).get();
        List<Address> addresses = customer.getAddresses();
        Iterable<Cart> cart = cartRepository.findAllByUserIdOrderByCreatedDateDesc(String.valueOf(userId));

        Optional<Address> address = addressRepository.findById(request.getId());

        if(address.isPresent()) {
            response.put("addresses", addresses);
            response.put("defaultId", address.get().getId());
            response.put("cart", cart);

            Iterable<TaxRate> tax = taxRepository.findByCity(address.get().getCity());
            Double subTotal = 0.0;
            for(Cart item: cart){
                subTotal += item.getQuantity() * item.getMovie().getPrice();
            }

            Double rate = 0.0775;
            for(TaxRate item: tax) {
                rate = item.getRate().doubleValue();
            }

            Double salesTax = subTotal * rate;
            Double total = salesTax + subTotal;

            response.put("salesTax", salesTax);
            response.put("total", total);
            response.put("subTotal", subTotal);
        }

        else {
            response.put("message", "Address not found");
            return new ResponseEntity<>(
                    response,
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                response,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> checkout(@PathVariable(value = "id") String id) {
        HashMap<String, Object> response = new HashMap<>();

        Optional<Customer> customer = customerRepository.findById(Integer.parseInt(id));
        Iterable<Cart> cart = cartRepository.findAllByUserIdOrderByCreatedDateDesc(id);

        response.put("address", customer.get().getPrimaryAddress());
        if(customer.isPresent()) {
            Integer primaryAddressId = customer.get().getPrimaryAddress();
            Optional<Address> address = addressRepository.findById(primaryAddressId);

            if(address.isPresent()) {
                response.put("address", address.get());
                Iterable<TaxRate> tax = taxRepository.findByCity(address.get().getCity());
                Double subTotal = 0.0;
                for(Cart item: cart){
                    subTotal += item.getQuantity() * item.getMovie().getPrice();
                }

                Double rate = 0.0775;
                for(TaxRate item: tax) {
                    rate = item.getRate().doubleValue();
                }

                Double salesTax = subTotal * rate;
                Double total = salesTax + subTotal;

                response.put("subTotal", subTotal);
                response.put("salesTax", salesTax);
                response.put("total", total);

            }
        }
        response.put("cart", cart);

        return new ResponseEntity<>(
                response,
                HttpStatus.OK);
    }


    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    @ResponseBody
    public String charge(@RequestBody ChargeRequest chargeRequest) throws StripeException
    {
        System.out.println(chargeRequest.getDescription());
        System.out.println(chargeRequest.getAmount());
        System.out.println(chargeRequest.getCurrency());

        PaymentIntent charge = paymentsService.createCharge(chargeRequest);
        Map<String, Object> map = new HashMap();
        map.put("id", charge.getId());
        map.put("amount", charge.getAmount());
        map.put("secret", charge.getClientSecret());
        map.put("currency", charge.getCurrency());
        map.put("created", charge.getCreated());


        Gson gson = new Gson();
        System.out.println(gson.toJson(map));
        return gson.toJson(map);
    }

    @ExceptionHandler(StripeException.class)
    @ResponseBody
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return model.toString();
    }
}