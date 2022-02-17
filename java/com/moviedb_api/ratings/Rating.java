package com.moviedb_api.ratings;

import com.fasterxml.jackson.annotation.JsonView;
import com.moviedb_api.Views;
import com.moviedb_api.movie.Movie;

import javax.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @Column(name = "movieid")
    private String movieId;

    @Column(name = "rating")
    private float rating;

    private Integer numVotes;

    @Column(name = "imdb")
    private String imdb;

    @Column(name = "metacritic")
    private String metacritic;

    @Column(name = "rottenTomatoes")
    private String rottenTomatoes;

    @Column(name = "rottenTomatoesAudience")
    private String rottenTomatoesAudience;

    @Column(name = "rottenTomatoesStatus")
    private String rottenTomatoesStatus;

    @Column(name = "rottenTomatoesAudienceStatus")
    private String rottenTomatoesAudienceStatus;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "movieId", insertable = false, updatable = false)
    //@Fetch(FetchMode.JOIN)
    @OneToOne(mappedBy = "ratings")
    private Movie movie;

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
}