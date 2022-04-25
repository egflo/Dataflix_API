package com.moviedb_api.ratings;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.moviedb_api.Views;
import com.moviedb_api.movie.Movie;
import com.moviedb_api.movie.MovieSimplified;
import org.springframework.context.annotation.Configuration;

import javax.persistence.*;

//@JsonFilter("ratingFilter")
@Entity
@Table(name = "ratings")
public class Rating {

    @JsonView({Views.Rating.class})
    @Id
    @Column(name = "movieid")
    private String movieId;

    @JsonView({Views.Rating.class})
    @Column(name = "rating")
    private float rating;

    @JsonView({Views.Rating.class})
    @Column(name = "numVotes")
    private Integer numVotes;

    @JsonView({Views.Rating.class})
    @Column(name = "imdb")
    private String imdb;

    @JsonView({Views.Rating.class})
    @Column(name = "metacritic")
    private String metacritic;

    @JsonView({Views.Rating.class})
    @Column(name = "rottenTomatoes")
    private String rottenTomatoes;

    @JsonView({Views.Rating.class})
    @Column(name = "rottenTomatoesAudience")
    private String rottenTomatoesAudience;

    @JsonView({Views.Rating.class})
    @Column(name = "rottenTomatoesStatus")
    private String rottenTomatoesStatus;

    @JsonView({Views.Rating.class})
    @Column(name = "rottenTomatoesAudienceStatus")
    private String rottenTomatoesAudienceStatus;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "movieId", insertable = false, updatable = false)
    //@Fetch(FetchMode.JOIN)
   // @JsonView({Views.Rating.class})
    //@ManyToOne
    //@JoinColumn(name = "movieId", referencedColumnName = "id", insertable = false, updatable = false)
    //private Movie movie;

    //@JsonBackReference
    @ManyToOne
    @JoinColumn(name = "movieId", referencedColumnName = "id", insertable = false, updatable = false)
    private MovieSimplified movie;

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {this.movieId = movieId;}

    @JsonView(Views.Public.class)
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {this.rating = rating;}

    @JsonView(Views.Public.class)
    public Integer getNumVotes() {
        return numVotes;
    }

    public void setNumVotes(int numVotes) {
        this.numVotes = numVotes;
    }

    @JsonView(Views.Public.class)
    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {this.imdb = imdb;}

    @JsonView(Views.Public.class)
    public String getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(String metacritic) {this.metacritic = metacritic;}

    @JsonView(Views.Public.class)
    public String getRottenTomatoes() {
        return rottenTomatoes;
    }

    public void setRottenTomatoes(String rottenTomatoes) {this.rottenTomatoes = rottenTomatoes;}

    @JsonView(Views.Public.class)
    public String getRottenTomatoesAudience() {
        return rottenTomatoesAudience;
    }

    public void setRottenTomatoesAudience(String rottenTomatoesAudience) {this.rottenTomatoesAudience = rottenTomatoesAudience;}

    @JsonView(Views.Public.class)
    public String getRottenTomatoesStatus() {
        return rottenTomatoesStatus;
    }

    public void setRottenTomatoesStatus(String rottenTomatoesStatus) {this.rottenTomatoesStatus = rottenTomatoesStatus;}

    @JsonView(Views.Public.class)
    public String getRottenTomatoesAudienceStatus() {
        return rottenTomatoesAudienceStatus;
    }

    public void setRottenTomatoesAudienceStatus(String rottenTomatoesAudienceStatus) {this.rottenTomatoesAudienceStatus = rottenTomatoesAudienceStatus;}

    public MovieSimplified getMovie() {
        return movie;
    }

    public void setMovie(MovieSimplified movie) {
        this.movie = movie;
    }

}

