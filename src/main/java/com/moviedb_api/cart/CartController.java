package com.moviedb_api.cart;


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
    private JwtTokenUtil authenticationService;

    @GetMapping("/")
    public ResponseEntity<?> getUserCartItems(@RequestHeader HttpHeaders headers, CartRequest request) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        return cartService.getCart(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> addCartItemById(@RequestHeader HttpHeaders headers, @PathVariable String id) {

        CartRequest request = new CartRequest();
        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);

        request.setUserId(userId);
        request.setMovieId(id);
        request.setQty(1);

        return cartService.addCart(request);
    }


    @PutMapping("/")
    public ResponseEntity<?> addUserCartItem(@RequestHeader HttpHeaders headers,
                                       @RequestBody CartRequest request) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        request.setUserId(userId);

        return cartService.addCart(request);
    }

    @PostMapping("/")
    public ResponseEntity<?> updateUserCartItem(@RequestHeader HttpHeaders headers,
                                             @RequestBody CartRequest request) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        request.setUserId(userId);

        return cartService.updateCart(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItemFromUsersCart(@RequestHeader HttpHeaders headers,
                                                     @PathVariable Integer id) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        CartRequest request = new CartRequest();
        request.setUserId(userId);
        request.setId(id);

        System.out.println("deleteItemFromUsersCart: " + id);
        return cartService.deleteCart(request);
    }

    @GetMapping("/qty/")
    public ResponseEntity<?>  getCartQty(
            @RequestHeader HttpHeaders headers) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        CartRequest request = new CartRequest();
        request.setUserId(userId);

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
    //public Map<String, Object> findbyUserId(
    public ResponseEntity<?> findCartItemsbyUserId(
            @PathVariable String id) {

        return cartService.getCart(id);
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<?> findCartItemsbyMovieId(
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
    public ResponseEntity<?> findCartItemsByCustomer(
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

    @PutMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartRequest request) {

        return cartService.addCart(request);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateToCart(@RequestBody CartRequest request) {

        return cartService.updateCart(request);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFromCart(@RequestBody CartRequest request) {

        return cartService.deleteCart(request);
    }
}
