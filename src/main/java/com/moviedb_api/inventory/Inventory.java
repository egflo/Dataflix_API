package com.moviedb_api.inventory;
import com.moviedb_api.movie.Movie;

import javax.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @Column(name= "productId")
    private String productId;

    @Column(name="status")
    private String status;

    @Column(name="qty")
    private Integer quantity;

    @OneToOne(mappedBy = "inventory")
    private Movie movie;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Movie getMovie() {
      return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}

