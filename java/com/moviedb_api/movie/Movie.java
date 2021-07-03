package com.moviedb_api.movie;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.moviedb_api.Views;
import com.moviedb_api.genres_in_movies.Genre_Movie;
import com.moviedb_api.order.Order;
import com.moviedb_api.price.Price;
import com.moviedb_api.ratings.Rating;
import com.moviedb_api.stars_in_movies.Star_Movie;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @Column(name = "id")
    @JsonView(Views.Summary.class)
    private String id;

    @Column(name="title")
    @JsonView(Views.Summary.class)
    private String title;

    @Column(name="year")
    @JsonView(Views.Summary.class)
    private int year;

    @Column(name="director")
    @JsonView(Views.Summary.class)
    private String director;

    @Column(name="poster")
    @JsonView(Views.Summary.class)
    private String poster;

    @Column(name="plot")
    @JsonView(Views.Summary.class)
    private String plot;

    @Column(name="rated")
    @JsonView(Views.Summary.class)
    private String rated;

    @Column(name="runtime")
    @JsonView(Views.Summary.class)
    private String runtime;

    @Column(name="language")
    @JsonView(Views.Public.class)
    private String language;

    @Column(name="writer")
    @JsonView(Views.Public.class)
    private String writer;

    @Column(name="awards")
    @JsonView(Views.Public.class)
    private String awards;

    @Column(name="boxOffice")
    @JsonView(Views.Public.class)
    private String boxOffice;

    @Column(name="production")
    @JsonView(Views.Public.class)
    private String production;

    @Column(name="country")
    @JsonView(Views.Public.class)
    private String country;

    @Column(name="background")
    @JsonView(Views.Public.class)
    private String background;

    @OneToMany(targetEntity = Genre_Movie.class, mappedBy = "movieId", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonView(Views.Public.class)
    private Set<Genre_Movie> genres;

    //@OneToMany(targetEntity = Rating.class, mappedBy = "movieId", orphanRemoval = true, fetch = FetchType.LAZY)
    //@JsonView(Views.Public.class)
    //private Set<Rating> ratings;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "movieid")
    @JsonView(Views.Public.class)
    private Rating ratings;

    @OneToMany(targetEntity = Star_Movie.class, mappedBy = "movieId", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonView(Views.Public.class)
    private Set<Star_Movie> cast;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "movieid")
    @JsonView(Views.Public.class)
    private Price price;

    //Setters and Getters
    public String getId() {
        return  id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return  title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return  year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDirector() {
        return  director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getPoster() {
        return  poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPlot() {
        return  plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getRated() {
        return  rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getRuntime() {
        return  runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getLanguage() {
        return  language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(String boxOffice) {
        this.boxOffice = boxOffice;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Set<Genre_Movie> getGenres() { return genres; }

    public void setGenres(Genre_Movie genre_movie) {this.genres.add(genre_movie); }

    //public Set<Rating> getRatings() {return ratings; }

    //public void setRating(Rating rating) {this.ratings.add(rating); }

    public Rating getRatings() {return ratings; }

    public void setRating(Rating ratings) {this.ratings = ratings; }

    public Set<Star_Movie> getCast() {return cast; }

    public void setCast(Star_Movie star_movie) {this.cast.add(star_movie); }

    public float getPrice() {return price.getPrice(); }

    public void setPrice(Price price) { this.price = price; }
}
