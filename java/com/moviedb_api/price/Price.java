package com.moviedb_api.price;

import com.moviedb_api.movie.Movie;

import javax.persistence.*;

@Entity
@Table(name = "movie_price")
public class Price {
    @Id
    @Column(name="movieId")
    private String movieId;

    @Column(name="price")
    private Double price;

    //@JoinColumn(name = "movieId", insertable = false, updatable = false)
    //@Fetch(FetchMode.JOIN)
    @OneToOne(mappedBy = "price")
    private Movie movie;

    //Setters and Getters
    public String getMovieId() {
        return  movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public Double getPrice() {
        return  price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
