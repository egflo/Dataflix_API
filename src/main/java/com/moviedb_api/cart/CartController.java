package com.moviedb_api.cart;


import com.moviedb_api.security.AuthenticationFacade;
import com.moviedb_api.security.JwtTokenUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


//https://www.amitph.com/spring-rest-http-header/
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private AuthenticationFacade authenticationFacade;


    @GetMapping("/")
    public ResponseEntity<?> getCart(@RequestHeader HttpHeaders headers, CartRequest request) {

        return cartService.getCart(authenticationFacade.getUserId());
    }


    @PostMapping("/")
    public ResponseEntity<?> addCart(@RequestHeader HttpHeaders headers, @RequestBody CartRequest request) {


        return cartService.addCart(request);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateCart(@RequestHeader HttpHeaders headers,
                                             @RequestBody CartRequest request) {


        return cartService.updateCart(request);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@RequestHeader HttpHeaders headers,
                                                     @PathVariable Integer id) {

        CartRequest request = new CartRequest();
        request.setId(id);

        return cartService.deleteCart(request);
    }

    @GetMapping("/qty/")
    public ResponseEntity<?>  getCartQty(
            @RequestHeader HttpHeaders headers) {

        CartRequest request = new CartRequest();
        request.setUserId(String.valueOf(authenticationFacade.getUserId()));
        return cartService.getCartQty(request);
    }


    /**
     *
     *    ADMIN METHODS
     * **/

    @GetMapping("/all")
    public ResponseEntity<?> findAll(
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy
    ) {

        return cartService.getAll(
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCartById(
            @PathVariable Integer id) {

        return cartService.getCart(id);
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<?> findCartByMovieId(
            @PathVariable String id,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy) {


        return cartService.findAllByMovieId(id,
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }

    @GetMapping("/customer/{id}")
    //public Map<String, Object> findbyUserId(
    public ResponseEntity<?> findCartByCustomer(
            @PathVariable Integer id,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy) {


        return cartService.findAllByUserId(id,
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }
}
