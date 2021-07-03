package com.moviedb_api.order;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.moviedb_api.movie.Movie;
import com.moviedb_api.sale.Sale;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "orderId")
    private int orderId;

    @Column(name = "movieId")
    private String movieId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "list_price")
    private float list_price;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "orderId", insertable = false, updatable = false)
    //@Fetch(FetchMode.JOIN)
    //private Sale sale;
   @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "orderId", referencedColumnName = "id", insertable = false, updatable = false)
    // @ManyToOne
    //@JoinColumn(name = "orderId", referencedColumnName = "id", insertable = false, updatable = false)
    //@JoinColumn(name="orderId", insertable = false, updatable = false)
    private Sale sale;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {this.quantity = quantity;}

    public float getList_price() {
        return list_price;
    }

    public void setList_price(float list_price) {this.list_price = list_price;}

}