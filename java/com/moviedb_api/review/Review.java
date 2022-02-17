package com.moviedb_api.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.moviedb_api.customer.Customer;
import com.moviedb_api.movie.Movie;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "movieId")
    private String movieId;

    @Column(name = "customerId")
    private Integer customerId;

    @Column(name = "text")
    private String text;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "sentiment")
    private String sentiment;

    @Column(name = "title")
    private String title;

    @Column(name = "created")
    private Date created;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = false)
    @JoinColumn(name = "customerId", insertable = false, updatable = false)
    private Customer customer;

   // @JsonManagedReference
    @OneToOne(
          cascade = CascadeType.ALL, orphanRemoval = true
    )
    @JoinColumn(name = "movieId", insertable = false, updatable = false)
    private Movie movie;

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {this.movieId = movieId;}

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {this.customerId = customerId;}

    public String getText() {
        return text;
    }

    public void setText(String text) {this.text = text;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {this.title = title;}

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {this.rating = rating;}

    public Date getCreated() { return created; }

    public void setCreated(Date created) { this.created = created; }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {this.sentiment = sentiment;}

    public Map<String, Object> getCustomer() {
        HashMap<String, Object> customer = new HashMap<>();
        if (this.customer != null) {
            customer.put("id", this.customer.getId());
            customer.put("id", this.customer.getId());
            customer.put("firstname", this.customer.getFirstname());
            customer.put("lastname", this.customer.getLastname());
            customer.put("email", this.customer.getEmail());
        }
        return customer;
    }

    public void setCustomer(Customer customer) {this.customer = customer;}

    public Map<String, Object> getMovie() {
        HashMap<String, Object> movie = new HashMap<>();

        if (this.movie != null) {
            movie.put("id", this.movie.getId());
            movie.put("title", this.movie.getTitle());
            movie.put("year", this.movie.getYear());
            movie.put("runtime", this.movie.getRuntime());
            movie.put("rated", this.movie.getRated());
            movie.put("poster", this.movie.getPoster());
        }

        return movie;
    }

    public void setMovie(Movie movie) {this.movie = movie;}
}

    /**
     *
     *
     public String getCustomerName() {
     if(customer != null) {
     return customer.getFirstName() + " " + customer.getLastName();
     }
     return "John Doe";
     */

