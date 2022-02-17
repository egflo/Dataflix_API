package com.moviedb_api.movie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.moviedb_api.Views;
import com.moviedb_api.genre.Genre;
import com.moviedb_api.inventory.Inventory;
import com.moviedb_api.price.Price;
import com.moviedb_api.ratings.Rating;
import com.moviedb_api.cast.Star_Movie;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @Column(name = "id")
    @JsonView(Views.Public.class)
    private String id;

    @Column(name="title")
    @JsonView(Views.Public.class)
    private String title;

    @Column(name="year")
    @JsonView(Views.Public.class)
    private int year;

    @Column(name="director")
    @JsonView(Views.Public.class)
    private String director;

    @Column(name="poster")
    @JsonView(Views.Public.class)
    private String poster;

    @Column(name="plot")
    @JsonView(Views.Public.class)
    private String plot;

    @Column(name="rated")
    @JsonView(Views.Public.class)
    private String rated;

    @Column(name="runtime")
    @JsonView(Views.Public.class)
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

    @JsonIgnore
    @Column(name="cached")
    private Integer cached;

    //@OneToMany(targetEntity = Genre_Movie.class, mappedBy = "movieId", orphanRemoval = true, fetch = FetchType.LAZY)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "genres_in_movies",
            joinColumns = @JoinColumn(name = "movieId"),
            inverseJoinColumns = @JoinColumn(name = "genreId")
    )
    @JsonView(Views.Public.class)
    private Set<Genre> genres;

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

    @Column(name="updated")
    private Date updated;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "productId")
    @JsonView(Views.Public.class)
    private Inventory inventory;

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

    public Set<Genre> getGenres() { return genres; }

    public void setGenres(Genre genre) {this.genres.add(genre); }

    public Rating getRatings() {return ratings; }

    public void setRating(Rating ratings) {this.ratings = ratings; }

    public Set<Star_Movie> getCast() {return cast; }

    public void setCast(Star_Movie star_movie) {this.cast.add(star_movie); }

    public Double getPrice() {return price.getPrice(); }

    public void setPrice(Price price) { this.price = price; }

    public Integer getCached() {
        return cached;
    }
    public void setCached(Integer cached) {
        this.cached = cached;
    }

    public Date getUpdated() {
        return updated;
    }
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Map<String, Object> getInventory() {
        Map<String, Object> map = new HashMap<>();
        map.put("quantity", inventory.getQuantity());
        map.put("status", inventory.getStatus());
        return map;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
