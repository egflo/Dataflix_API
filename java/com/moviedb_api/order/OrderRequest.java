package com.moviedb_api.order;

import java.sql.Date;

public class OrderRequest {
    private Integer id;
    private Integer saleId;
    private String movieId;
    private Integer quantity;
    private Double price;


    public OrderRequest() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public String getMovieId() {
        return movieId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


}