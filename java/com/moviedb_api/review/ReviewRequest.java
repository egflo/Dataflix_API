package com.moviedb_api.review;

public class ReviewRequest {
    private int id;

    private String movieId;

    private Integer customerId;

    private String text;

    private Integer rating;

    private String title;

    public void setId(int id) { this.id = id; }

    public int getId() { return id; }

    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getMovieId() { return movieId; }

    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getCustomerId() { return customerId; }

    public void setText(String text) { this.text = text; }

    public String getText() { return text; }

    public void setRating(Integer rating) { this.rating = rating; }

    public Integer getRating() { return rating; }

    public void setTitle(String title) { this.title = title; }

    public String getTitle() { return title; }

}
