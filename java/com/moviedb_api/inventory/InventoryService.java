package com.moviedb_api.inventory;

import com.moviedb_api.cart.Cart;
import com.moviedb_api.cart.CartRepository;
import com.moviedb_api.cart.CartRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class InventoryService {

    private final CartRepository cartRepository;
    private final InventoryRepository inventoryRepository;


    public InventoryService(CartRepository cartRepository
            , InventoryRepository inventoryRepository) {
        this.cartRepository = cartRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public String inventoryStatus(Integer quantity) {
        String status = "in stock";
        if (quantity == 0) {
            status = "out of stock";
        }
        else if (quantity <= 5 && quantity > 0) {
            status = "limited";
        }
        return status;
    }

    public Boolean checkInventory(CartRequest cartRequest) {
        System.out.println("checkInventory");

        //New cart request - add to cart
        if (cartRequest.getId() == null) {
            Optional<Inventory> inventory = inventoryRepository.findById(cartRequest.getMovieId());
            if (inventory.isPresent()) {
                if (inventory.get().getQuantity() >= cartRequest.getQty()) {
                    return true;
                }
                return false;
            }
            return false;
        }

        //Existing cart request - update cart
        Cart cart = cartRepository.findById(cartRequest.getId()).get();
        Optional<Inventory> inventory = inventoryRepository.findInventoryByProductId(cartRequest.getMovieId());
        System.out.println("Product ID: " + cartRequest.getMovieId());
        System.out.println("Inventory quantity: " + inventory.get().getQuantity());
        System.out.println("Intial Cart quantity: " + cart.getQuantity());
        System.out.println("Request Cart quantity: " + cartRequest.getQty());
        if (!inventory.isPresent()) {
            return false;
        }
        //Increasing the quantity of the product's inventory
        if(cartRequest.getQty() < cart.getQuantity()) {
            return true;
        }
        //Decreasing the quantity of the product, but not below 0
        if (inventory.get().getQuantity() >= cartRequest.getQty()) {
            return true;
        }
        return false;
    }

    public ResponseEntity<?> addInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = new Inventory();
        inventory.setProductId(inventoryRequest.getProductId());
        inventory.setQuantity(inventoryRequest.getQuantity());
        inventory.setStatus(inventoryStatus(inventoryRequest.getQuantity()));
        //inventory.setCreatedAt(new Date());
        //inventory.setUpdatedAt(new Date());
        inventoryRepository.save(inventory);
        return ResponseEntity.ok(inventory);
    }

    public ResponseEntity<?> updateInventory(InventoryRequest inventoryRequest) {
        Optional<Inventory> inventory = inventoryRepository.findById(inventoryRequest.getProductId());
        if (inventory.isPresent()) {
            inventory.get().setProductId(inventoryRequest.getProductId());
            inventory.get().setQuantity(inventoryRequest.getQuantity());
            inventory.get().setStatus(inventoryStatus(inventoryRequest.getQuantity()));

            return ResponseEntity.ok(inventoryRepository.save(inventory.get()));

        }

        return ResponseEntity.badRequest().body("Inventory not found");
    }


    public ResponseEntity<?> updateInventoryFromCartAdd(CartRequest cartRequest) {

        if (checkInventory(cartRequest)) {
            Inventory inventory = inventoryRepository.findInventoryByProductId(cartRequest.getMovieId()).get();
            inventory.setQuantity(inventory.getQuantity() - cartRequest.getQty());
            inventory.setStatus(inventoryStatus(inventory.getQuantity()));

            return ResponseEntity.ok(inventory);
        }

        return ResponseEntity.badRequest().body("Not enough inventory");
    }

    public ResponseEntity<?> updateInventoryFromCartRemove(CartRequest cartRequest) {
        Cart cart = cartRepository.findById(cartRequest.getId()).get();
        Inventory inventory = inventoryRepository.findById(cart.getMovieId()).get();
        inventory.setQuantity(inventory.getQuantity() + cart.getQuantity());
        inventory.setStatus(inventoryStatus(inventory.getQuantity()));
        inventoryRepository.save(inventory);
        return ResponseEntity.ok(inventory);
    }


    public ResponseEntity<?> updateInventory(CartRequest cartRequest) {
        Cart cart = cartRepository.findById(cartRequest.getId()).get();
        if (checkInventory(cartRequest)) {
            Inventory inventory = inventoryRepository.findInventoryByProductId(cartRequest.getMovieId()).get();

            //Increasing the quantity of the product
            if(cartRequest.getQty() < cart.getQuantity()) {
                Integer diff = cart.getQuantity() - cartRequest.getQty();
                inventory.setQuantity(inventory.getQuantity() + diff);
            }
            //Decreasing the quantity of the product
            else {
                inventory.setQuantity(inventory.getQuantity() - cartRequest.getQty());
            }
            inventory.setStatus(inventoryStatus(inventory.getQuantity()));

            return ResponseEntity.ok(inventory);
        }

        return ResponseEntity.badRequest().body("Not enough inventory");
    }

    public ResponseEntity<?> deleteInventory(String id) {
        Optional<Inventory> inventory = inventoryRepository.findById(id);
        if (inventory.isPresent()) {
            inventoryRepository.delete(inventory.get());
            return ResponseEntity.ok(inventory);
        }

        return ResponseEntity.badRequest().body("Inventory not found");
    }

    public ResponseEntity<?> findAll(Optional<String> status, PageRequest pageable) {

        if(status.isPresent()) {
            return ResponseEntity.ok(inventoryRepository.findInventoryByStatus(status.get(), pageable));
        }
        // This returns a JSON or XML with the movies
        return ResponseEntity.ok(inventoryRepository.findAll(pageable));
    }

    public ResponseEntity<?> findByProductId(String productId) {
        Optional<Inventory> inventory = inventoryRepository.findInventoryByProductId(productId);
        if (inventory.isPresent()) {
            return ResponseEntity.ok(inventory);
        }
        return ResponseEntity.badRequest().body("Inventory not found");
    }

    public ResponseEntity<?> searchInventory(String search, Optional<String> status, PageRequest pageable) {
        if(status.isPresent()) {
            return ResponseEntity.ok(inventoryRepository.findMoviesByIdOrTitleContainingAndStatus(search, status.get(), pageable));
        }
        // This returns a JSON or XML with the movies
        return ResponseEntity.ok(inventoryRepository.findMoviesByIdOrTitleContaining(search, pageable));
    }

}