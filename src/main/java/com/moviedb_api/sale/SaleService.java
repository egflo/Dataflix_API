package com.moviedb_api.sale;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.moviedb_api.HttpResponse;
import com.moviedb_api.cart.Cart;
import com.moviedb_api.cart.CartRepository;
import com.moviedb_api.checkout.ChargeController;
import com.moviedb_api.checkout.StripeService;
import com.moviedb_api.order.Order;
import com.moviedb_api.order.OrderRepository;
import com.moviedb_api.shipping.Shipping;
import com.moviedb_api.shipping.ShippingRepository;
import com.moviedb_api.shipping.ShippingRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import org.json.HTTP;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Transactional
public class SaleService {
    private final SaleRepository saleRepository;

    private final ShippingRepository shippingRepository;

    private final CartRepository cartRepository;

    private final OrderRepository orderRepository;


    @Autowired
    private StripeService paymentsService;


    public SaleService(SaleRepository saleRepository,
                       ShippingRepository shippingRepository,
                       CartRepository cartRepository,
                       OrderRepository orderRepository) {
        this.saleRepository = saleRepository;
        this.shippingRepository = shippingRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    public ResponseEntity<?> getSalesByCustomerId(Integer userId, PageRequest pagable){
        Page<Sale> sales = saleRepository.findSaleByCustomerId(userId, pagable);

        return ResponseEntity.ok(sales);
    }

    public ResponseEntity<?> searchSale(Integer id) {

        Map<String, Object> objectMap = new HashMap<>();

        try {
            Optional<Sale> sale = saleRepository.findById(id);
            if(sale.isPresent()){
                PaymentIntent paymentIntent = paymentsService.retriveCharge(sale.get().getStripeId());
                ChargeCollection charges = paymentIntent.getCharges();
                Charge charge = charges.getData().get(0);
                Charge.PaymentMethodDetails paymentMethodDetails = charge.getPaymentMethodDetails();
                Charge.PaymentMethodDetails.Card card = paymentMethodDetails.getCard();

                Map<String, Object> card_object = new HashMap<>();
                card_object.put("country", card.getCountry());
                card_object.put("exp_month", card.getExpMonth());
                card_object.put("exp_year", card.getExpYear());
                card_object.put("funding", card.getFunding());
                card_object.put("brand", card.getBrand());
                card_object.put("last4", card.getLast4());
                card_object.put("network", card.getNetwork());


                objectMap.put("sale", sale.get());
                objectMap.put("card", card_object);

                return ResponseEntity.ok(objectMap);
            }

            HttpResponse response = new HttpResponse();
            response.setMessage("Sale not found");
            response.setStatus(404);
            response.setSuccess(false);

            return ResponseEntity.status(404).body(response);

        } catch (Exception e) {

            System.out.println(e);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(objectMap);
    }

    public ResponseEntity<?> addSale(SaleRequest request) {
        Sale sale = new Sale();
        sale.setCustomerId(request.getCustomerId());
        sale.setSalesTax(request.getSalesTax());
        sale.setSaleDate(new Date());
        sale.setSubTotal(request.getSubTotal());
        sale.setTotal(request.getTotal());
        sale.setStripeId(request.getStripeId());
        sale.setStatus("pending");
        sale.setDevice(request.getDevice());

        Sale insert = saleRepository.save(sale);
        Integer saleId = insert.getId();

        ShippingRequest shippingRequest  = request.getShipping();

        Shipping shipping = new Shipping();

        shipping.setOrderId(saleId);
        shipping.setCustomerId(request.getCustomerId());
        shipping.setFirstname(shippingRequest.getFirstname());
        shipping.setLastname(shippingRequest.getLastname());
        shipping.setUnit(shippingRequest.getUnit());
        shipping.setStreet(shippingRequest.getStreet());
        shipping.setCity(shippingRequest.getCity());
        shipping.setState(shippingRequest.getState());
        shipping.setPostcode(shippingRequest.getPostcode());

        shippingRepository.save(shipping);

        Iterable<Cart> items = cartRepository.findAllByUserId(String.valueOf(request.getCustomerId()));

        ArrayList<Order> orders = new ArrayList<>();
        for(Cart cart: items) {
            Order order = new Order();

            order.setOrderId(saleId);
            order.setMovieId(cart.getMovieId());
            order.setQuantity(cart.getQuantity());
            order.setListPrice(cart.getMovie().getPrice());

            orders.add(order);
        }

        orderRepository.saveAll(orders);

        return  ResponseEntity.ok(sale);
    }


    public ResponseEntity<?> updateSale(SaleRequest request){
        Optional<Sale> update = saleRepository.findById(request.getId());

        if(update.isPresent()){
            Sale sale = update.get();

            sale.setId(request.getId());
            sale.setCustomerId(request.getCustomerId());
            sale.setSalesTax(request.getSalesTax());
            sale.setSaleDate(request.getSaleDate());
            sale.setSubTotal(request.getSubTotal());
            sale.setTotal(request.getTotal());

            return ResponseEntity.ok(saleRepository.save(sale));

        }

        HttpResponse response = new HttpResponse();
        response.setMessage("Sale not found");
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setSuccess(false);

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(response);

    }

    public ResponseEntity<?> deleteSale(SaleRequest request){

        Optional<Sale> sale = saleRepository.findById(request.getId());

        if(sale.isPresent()) {
            saleRepository.delete(sale.get());
            HttpResponse response = new HttpResponse();
            response.setMessage("Sale deleted");
            response.setStatus(HttpStatus.OK.value());
            response.setSuccess(true);
            return ResponseEntity.ok(response);
        }

        HttpResponse response = new HttpResponse();
        response.setMessage("Sale not found");
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setSuccess(false);

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(response);
    }

    public ResponseEntity<?> getMetadata(Optional<Integer> days){
        // This returns a JSON or XML with the movies
        Map<String, Object> response = new HashMap<>();
        response.put("total_transactions", saleRepository.count());
        response.put("total_sales", saleRepository.findTotalSales());

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -days.orElse(7));

        List<Integer> this_year = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        for (int i = 1; i <= days.orElse(7); i++) {
            c.add(Calendar.DAY_OF_YEAR, 1);
            Date date = c.getTime();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            long sales = saleRepository.findAllSalesFromDate(sqlDate);
            this_year.add((int)sales);
            dates.add(new SimpleDateFormat("MMM dd").format(sqlDate));
        }

        c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -days.orElse(7));
        c.add(Calendar.YEAR, -1); //Subtract 1 year

        List<Integer> last_year = new ArrayList<>();
        for (int i = 1; i <= days.orElse(7); i++) {
            c.add(Calendar.DAY_OF_YEAR, 1);
            Date date = c.getTime();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            long sales = saleRepository.findAllSalesFromDate(sqlDate);

            //String month = new SimpleDateFormat("MMM").format(sqlDate);
            //String day = new SimpleDateFormat("dd").format(sqlDate);
            //dv.date = month + " " + day;
            //dv.count = (int) sales;
            //last_year.dates.add(month + " " + day);
            //last_year.sales.add((int)sales);
            last_year.add((int)sales);
        }

        Gson gson = new Gson();
        HashMap<String, Object> data = new HashMap<>();
        data.put("browser", saleRepository.findAllSalesFromDevice("browser"));
        data.put("ios", saleRepository.findAllSalesFromDevice("ios"));
        data.put("android", saleRepository.findAllSalesFromDevice("android"));

        response.put("dates",dates);
        response.put("this_year", this_year);
        response.put("last_year", last_year);
        response.put("device_sales", data);



        c = Calendar.getInstance();
        c.setTime(new Date());
        Integer year = c.get(Calendar.YEAR);
        Integer month = c.get(Calendar.MONTH);

        long last_month_sales = saleRepository.findAllSalesFromMonthAndYear(month, year);
        long this_month_sales = saleRepository.findAllSalesFromMonthAndYear(month + 1, year);
        double percent = ((this_month_sales - last_month_sales) * 100) / this_month_sales;

        response.put("monthly_transactions", this_month_sales);
        response.put("monthly_change", percent);

        return new ResponseEntity<>(
                response,
                HttpStatus.OK);
    }

    public ResponseEntity<?> search(String search, Optional<String> status, PageRequest pagable) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        boolean isNumber = pattern.matcher(search).matches();
        Page<Sale> sales = null;

        if(isNumber) {
            if(status.isPresent()) {
                sales = saleRepository.findSaleByIdOrCustomerIdAndStatus(
                        Integer.parseInt(search),
                        status.get(),
                        pagable);

            }

            sales = saleRepository.findSaleByIdOrCustomerId(
                    Integer.parseInt(search),
                    pagable);
        }

        else {
            if (status.isPresent()) {
                sales = saleRepository.findAllSalesFromStringAndStatus(
                        search,
                        status.get(),
                        pagable);
            }
            sales = saleRepository.findAllSalesFromString(
                    search,
                    pagable);
        }

        return new ResponseEntity<>(
                sales,
                HttpStatus.OK);
    }
}
