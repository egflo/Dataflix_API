package com.moviedb_api.price;

import com.fasterxml.jackson.annotation.JsonView;
import com.moviedb_api.Views;
import com.moviedb_api.genres_in_movies.Genre_Movie;
import com.moviedb_api.movie.Movie;
import com.moviedb_api.ratings.Rating;
import com.moviedb_api.stars_in_movies.Star_Movie;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "movie_price")
public class Price {
    @Id
    @Column(name="movieId")
    private String movieId;

    private float price;

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

    public float getPrice() {
        return  price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

}
