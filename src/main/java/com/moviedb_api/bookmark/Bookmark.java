package com.moviedb_api.bookmark;

import com.moviedb_api.movie.Movie;
import com.moviedb_api.movie.MovieSimplified;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bookmark")
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "customerId")
    private Integer customerId;

    @Column(name = "movieId")
    private String movieId;

    @Column(name = "created")
    private Date created;

    @ManyToOne
    @JoinColumn(name = "movieId", referencedColumnName = "id", insertable = false, updatable = false)
    private MovieSimplified movie;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {this.customerId = customerId;}

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {this.created = created;}

    public MovieSimplified getMovie() {
        return movie;
    }

    public void setMovie(MovieSimplified movie) {
        this.movie = movie;
    }
}