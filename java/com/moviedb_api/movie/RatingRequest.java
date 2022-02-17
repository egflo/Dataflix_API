package com.moviedb_api.movie;

public class RatingRequest {
    private String movieId;

    private String imdb;

    private String metacritic;

    private String rottenTomatoes;

    private String rottenTomatoesAudience;

    public RatingRequest() {
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getIMDB() {
        return imdb;
    }

    public void setIMDB(String imdb) {
        this.imdb = imdb;
    }

    public String getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(String metacritic) {
        this.metacritic = metacritic;
    }

    public String getRottenTomatoes() {
        return rottenTomatoes;
    }

    public void setRottenTomatoes(String rottenTomatoes) {
        this.rottenTomatoes = rottenTomatoes;
    }

    public String getRottenTomatoesAudience() {
        return rottenTomatoesAudience;
    }

    public void setRottenTomatoesAudience(String rottenTomatoesAudience) {
        this.rottenTomatoesAudience = rottenTomatoesAudience;
    }

}
