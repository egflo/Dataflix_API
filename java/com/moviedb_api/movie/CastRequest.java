package com.moviedb_api.movie;

public class CastRequest {
    private String starId;

    private String movieId;

    private String category;

    private String characters;

    public String getStarId() {
        return starId;
    }

    public void setStarId(String starId) {
        this.starId = starId;
    }

    public String getMovieId() {
       return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
   }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCharacters() {
        return characters;
    }

    public void setCharacters(String characters) {
        this.characters = characters;
    }
}
