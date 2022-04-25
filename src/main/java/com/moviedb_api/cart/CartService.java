package com.moviedb_api.cart;

import com.google.gson.Gson;
import com.moviedb_api.HttpResponse;
import com.moviedb_api.inventory.Inventory;
import com.moviedb_api.inventory.InventoryRepository;
import com.moviedb_api.inventory.InventoryService;
import org.json.HTTP;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final InventoryService inventoryService;

    public CartService(CartRepository cartRepository, InventoryService inventoryService) {
        this.cartRepository = cartRepository;
        this.inventoryService = inventoryService;
    }

    public ResponseEntity<?> getAll(Pageable pagable) {
        return ResponseEntity.ok(cartRepository.findAll(pagable));
    }


    public ResponseEntity<?> getCartQty(CartRequest request) {

        String userId = request.getUserId();
        Iterable<Cart> items =  cartRepository.findAllByUserIdOrderByCreatedDateDesc(userId);

        Integer count = 0;

        for(Cart cart: items) {
            Integer cart_qty = cart.getQuantity();
            count += cart_qty;
        }

        return ResponseEntity.ok(count);

    }

    public ResponseEntity<?> findAllByMovieId(String movieId, Pageable pagable) {

        Page<Cart> carts = cartRepository.findAllByMovieId(movieId, pagable);

        return ResponseEntity.ok(carts);
    }

    public ResponseEntity<?> findAllByUserId(Integer userId, Pageable pagable) {

        return ResponseEntity.ok(cartRepository.findAllByUserId(userId, pagable));
    }

    public ResponseEntity<?> getCartCheckout(CartRequest request) {

        String userId = request.getUserId();
        Iterable<Cart> items = cartRepository.findAllByUserIdOrderByCreatedDateDesc(userId);

        Map<String, Object> content = new HashMap<>();

        Double subTotal = 0.0;
        for(Cart item: items) {
            subTotal += item.getQuantity() * item.getMovie().getPrice();

        }
        content.put("subtotal", subTotal);
        content.put("items", items);

        return ResponseEntity.ok(content);

    }

    public ResponseEntity<?> getCart(String userId) {
        Iterable<Cart> items = cartRepository.findAllByUserId(userId);

        return ResponseEntity.ok(items);
    }

    public ResponseEntity<?> getCartItem(CartRequest request) {

        Optional<Cart> cart = cartRepository.findCartById(request.getId());
        if(cart.isPresent()) {

            return ResponseEntity.ok(cart.get());
        }

        HttpStatus status = HttpStatus.NOT_FOUND;
        HttpResponse response = new HttpResponse();
        response.setStatus(status.value());
        response.setMessage("Cart not found");
        response.setSuccess(false);

        return ResponseEntity.status(status).body(response);
    }


    public ResponseEntity<?> addCart(CartRequest request){

        System.out.println("addCart");
        System.out.println(request.getUserId());
        System.out.println(request.getMovieId());
        System.out.println(request.getQty());

       Boolean inventory = inventoryService.checkInventory(request);

       System.out.println(inventory);

        if(inventory) {
            Cart cart = new Cart();
            cart.setUserId(request.getUserId());
            cart.setMovieId(request.getMovieId());
            cart.setQuantity(request.getQty());
            cart.setCreatedDate(new Date());

            System.out.println("Update inventory");
            ResponseEntity status = inventoryService.updateInventoryFromCartAdd(request);

            if(!status.getStatusCode().is2xxSuccessful()) {

                System.out.println("Error updating inventory");
                HttpResponse response = new HttpResponse();
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Quantity not available");
                response.setSuccess(false);

                return ResponseEntity.badRequest().body(response);
            }

            System.out.println("Save cart");
            Optional<Cart>
                    cart_duplicate = cartRepository.findCartByUserIdAndMovieId(cart.getUserId(), cart.getMovieId());

            if(cart_duplicate.isPresent()) {
                cart.setId(cart_duplicate.get().getId());
                cart.setQuantity(cart_duplicate.get().getQuantity() + 1);
                cart.setCreatedDate(cart_duplicate.get().getCreatedDate());
            }

            return ResponseEntity.ok(cartRepository.save(cart));
        }

        HttpResponse response = new HttpResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Product not available");
        response.setSuccess(false);

        return ResponseEntity.badRequest().body(response);
    }


    public ResponseEntity<?> updateCart(CartRequest request){

        Optional<Cart> update = cartRepository.findCartById(request.getId());

        if(update.isPresent()) {

            ResponseEntity inventory = inventoryService.updateInventory(request);

            if(inventory.getStatusCode().is2xxSuccessful()) {
                Cart cart_update = new Cart();

                cart_update.setId(update.get().getId());
                cart_update.setUserId(update.get().getUserId());
                cart_update.setMovieId(update.get().getMovieId());
                cart_update.setQuantity(request.getQty());
                cart_update.setCreatedDate(update.get().getCreatedDate());

                HttpResponse response = new HttpResponse();
                response.setStatus(HttpStatus.OK.value());
                response.setMessage("Cart Updated");
                response.setSuccess(true);
                Cart save = cartRepository.save(cart_update);
                response.setData(save);


                System.out.println("Update Cart Success");
                System.out.println(new Gson().toJson(response));

                return ResponseEntity.ok(response);
            }

            HttpResponse response = new HttpResponse();
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Quantity exceeds the available stock");
            response.setSuccess(false);
            response.setData(update.get());

            System.out.println("Update Cart Failed");
            return ResponseEntity.badRequest().body(response);
        }

        HttpResponse response = new HttpResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage("Cart not found");
        response.setSuccess(false);

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> deleteCart(CartRequest cartRequest) {

        Optional<Cart> cart = cartRepository.findCartById(cartRequest.getId());

        if(cart.isPresent()) {
            System.out.println("Cart found");
            inventoryService.updateInventoryFromCartRemove(cartRequest);
            cartRepository.delete(cart.get());

            HttpResponse response = new HttpResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Cart deleted");
            response.setSuccess(true);
            response.setData(cart.get());

            return ResponseEntity.ok(response);
        }

        HttpResponse response = new HttpResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Cart not found");
        response.setSuccess(false);

        return ResponseEntity.badRequest().body(response);
    }
}