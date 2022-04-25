package com.moviedb_api.checkout;

import org.json.JSONObject;

public class CheckoutRequest {
    private Integer id;
    private String userId;
    private String movieId;
    private Integer qty;

    public CheckoutRequest() {

    }

    public CheckoutRequest(String request) {
        JSONObject cartRequest = new JSONObject(request);
        this.userId = cartRequest.getString("userId");
        this.movieId = cartRequest.getString("movieId");
        this.qty = cartRequest.getInt("qty");
    }

    public Integer getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
