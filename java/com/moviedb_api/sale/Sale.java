package com.moviedb_api.sale;

import com.moviedb_api.genres_in_movies.Genre_Movie;
import com.moviedb_api.order.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "customerId")
    private int customerId;

    @Column(name = "saleDate")
    private String saleDate;

    @OneToMany(targetEntity= Order.class, mappedBy = "orderId", orphanRemoval = false, fetch = FetchType.LAZY)
    //@ManyToMany(targetEntity= Order.class, mappedBy = "orderId", fetch = FetchType.LAZY)

   // @OneToMany(mappedBy="sale", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<Order>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {this.saleDate = saleDate;}

    public List<Order> getOrders() {return orders; }

    public void setOrders(Order order) {this.orders.add(order); }


}