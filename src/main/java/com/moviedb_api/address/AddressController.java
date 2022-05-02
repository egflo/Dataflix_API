package com.moviedb_api.address;


import com.moviedb_api.customer.*;
import com.moviedb_api.security.JwtTokenUtil;
import com.moviedb_api.user_address.User_AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller // This means that this class is a Controller
@RequestMapping(path="/address") // This means URL's start with /demo (after Application path)
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private JwtTokenUtil authenticationService;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<?> getAddresses(
            @RequestHeader HttpHeaders headers) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);

        return addressService.getAddressesByUserId(Integer.parseInt(userId));
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<?> getAddressById(@PathVariable(value = "id") Integer id)
    {

        return addressService.getAddress(id);
    }

    @PutMapping("/")
    @ResponseBody
    public ResponseEntity<?> addAddress(
            @RequestHeader HttpHeaders headers,
            @RequestBody AddressRequest request) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        request.setUserId(Integer.parseInt(userId));

        return addressService.createAddress(request);
    }

    @PostMapping("/{id}")
    public @ResponseBody ResponseEntity<?>  updateAddressById(
            @PathVariable(value = "id") Integer id,
            @RequestBody AddressRequest request)
    {
        request.setId(id);
        return addressService.updateAddress(request);
    }

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<?> updateAddress(
            @RequestHeader HttpHeaders headers,
            @RequestBody AddressRequest request) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        request.setUserId(Integer.parseInt(userId));

        return addressService.updateAddress(request);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteAddress(
            @RequestHeader HttpHeaders headers,
            @PathVariable(value = "id") Integer id) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        return addressService.deleteAddress(id);
    }

    @PostMapping("/primary/{id}")
    @ResponseBody
    public ResponseEntity<?> makePrimary(
            @RequestHeader HttpHeaders headers,
            @PathVariable(value = "id") Integer id) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);

        return addressService.makeAddressPrimary(id, Integer.parseInt(userId));
    }


    /**
     *
     *    ADMIN METHODS
     * **/

    @GetMapping(path="/all")
    public @ResponseBody
    ResponseEntity<?> getAllUsers(
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy
    ) {
        // This returns a JSON or XML with the movies
        return addressService.getAllAddresses(
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }

    @GetMapping("/firstname/{fname}")
    public ResponseEntity<?> getAddressByFirstName(
            @PathVariable(value = "fname") String fname,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy
    ) {
        // This returns a JSON or XML with the movies
        return addressService.getAddressesByFirstName(
                fname,
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }

    @GetMapping("/lastname/{lname}")
    public ResponseEntity<?> getCustomerByLastName(
            @PathVariable(value = "lname") String lname,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy
    ) {
        // This returns a JSON or XML with the movies
        return addressService.getAddressesByLastName(
                lname,
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }

    @GetMapping("/postcode/{postcode}")
    public ResponseEntity<?> getCustomerByPostCode(
            @PathVariable(value = "postcode") String postcode,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy
    ) {
        // This returns a JSON or XML with the movies
        return addressService.getAddressesByPostcode(
                postcode,
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }

    @GetMapping("/customer/{id}")
    public @ResponseBody ResponseEntity<?> getCustomerById(@PathVariable(value = "id") Integer id)
    {
        return addressService.getAddress(id);
    }

}