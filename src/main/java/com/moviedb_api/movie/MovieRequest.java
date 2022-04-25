package com.moviedb_api.movie;

import java.util.List;

public class MovieRequest {

    private String id;

    private String title;

    private int year;

    private String director;

    private String poster;

    private String plot;

    private String rated;

    private String runtime;

    private String language;

    private String writer;

    private String awards;

    private String boxOffice;

    private String production;

    private String country;

    private String background;

    private Double price;

    private RatingRequest ratings;

    //@JsonProperty("GenreRequest")
    private GenreRequest[] genres;

   //@JsonProperty("CastRequest")
    private CastRequest[] casts;

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

    public void setPrice(Double price) { this.price = price; }

    public Double getPrice() { return price; }

    public RatingRequest getRatings() { return ratings; }

    public void setRatings(RatingRequest ratings) { this.ratings = ratings; }

    public List<GenreRequest> getGenres() { return List.of(genres); }

    public void setGenres(GenreRequest[] genres) {this.genres = genres; }

    public List<CastRequest>  getCasts() { return List.of(casts); }

    public void setCast(CastRequest[] casts) { this.casts = casts; }

}
