package com.moviedb_api.order;

import com.moviedb_api.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public ResponseEntity<?> getOrder(OrderRequest request){

        Optional<Order> order = orderRepository.findById(request.getId());

        if(order.isPresent()) {
            return ResponseEntity.ok(order.get());
        }

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> addOrders(Iterable<OrderRequest> requests){

        ArrayList<Order> orders = new ArrayList<>();
        for(OrderRequest request: requests) {

            Order order = new Order();
            order.setOrderId(request.getSaleId());
            order.setMovieId(request.getMovieId());
            order.setQuantity(request.getQuantity());
            order.setListPrice(request.getPrice());

            orders.add(order);
        }

        return ResponseEntity.ok(orderRepository.saveAll(orders));
    }

    public ResponseEntity<?> addOrder(OrderRequest request){

        Order order = new Order();
        order.setOrderId(request.getSaleId());
        order.setMovieId(request.getMovieId());
        order.setQuantity(request.getQuantity());
        order.setListPrice(request.getPrice());

        return ResponseEntity.ok(orderRepository.save(order));
    }


    public ResponseEntity<?> updateOrder(OrderRequest request){

        Optional<Order> update = orderRepository.findById(request.getId());

        if(update.isPresent()) {
            Order order = new Order();

            order.setId(update.get().getId());
            order.setOrderId(request.getSaleId());
            order.setMovieId(request.getMovieId());
            order.setQuantity(request.getQuantity());
            order.setListPrice(request.getPrice());

            return ResponseEntity.ok(orderRepository.save(order));
        }

        HttpResponse response = new HttpResponse();
        response.setMessage("Order not found");
        response.setStatus(404);
        response.setSuccess(false);

        return ResponseEntity.notFound().build();

    }


    public ResponseEntity<?> deleteSale(OrderRequest request){

        Optional<Order> order = orderRepository.findById(request.getId());

        if(order.isPresent()) {
            orderRepository.delete(order.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

}
