package com.moviedb_api.shipping;

import com.moviedb_api.review.Review;
import com.moviedb_api.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller // This means that this class is a Controller
@RequestMapping(path="/shipping") // This means URL's start with /movies (after Application path)
public class ShippingController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private ShippingService shippingService;

    @GetMapping(path="/all")
    public @ResponseBody
    ResponseEntity<?> getAllShipping(
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy)

    {
        return shippingService.getAllAddresses(
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }

    @GetMapping(path="/customer/{id}")
    public @ResponseBody
    ResponseEntity<?> getShippingByCustomer(
            @PathVariable Integer id,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page
    ){
        return shippingService.getAddressByCustomerId(id);
    }

    @GetMapping(path="/sale/{id}")
    public @ResponseBody
    ResponseEntity<?> getShippingBySaleId(
            @PathVariable Integer id,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page
    ){
        return shippingService.getAddressByOrderId(id);
    }

    @PostMapping(path="/sale/{id}")
    public @ResponseBody
    ResponseEntity<?> updateShippingByOrderId(
            @PathVariable Integer id,
            @RequestBody ShippingRequest request
    ){
        return shippingService.updateAddressByOrderId(id, request);
    }


    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<?> findShippingById(@PathVariable(value = "id") Integer id)
    {

        return shippingService.getAddress(id);
    }

    @PutMapping("/")
    public @ResponseBody
    ResponseEntity<?> createShipping(@RequestBody ShippingRequest request)
    {
        return shippingService.createAddress(request);
    }

    @PostMapping("/{id}")
    public @ResponseBody
    ResponseEntity<?> updateShipping(@PathVariable(value = "id") Integer id,
                                     @RequestBody ShippingRequest request)
    {
        return shippingService.updateAddress(request);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<?> deleteShipping(@PathVariable(value = "id") Integer id)
    {
        return shippingService.deleteAddress(id);
    }

}