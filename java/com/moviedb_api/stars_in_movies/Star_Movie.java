package com.moviedb_api.stars_in_movies;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.moviedb_api.Views;
import com.moviedb_api.genres_in_movies.Genre_Movie;
import com.moviedb_api.movie.Movie;
import com.moviedb_api.star.Star;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "stars_in_movies")
public class Star_Movie  {
    @Id
    @Column(name = "id")
    private int id;


    @Column(name = "starId")
    private String starId;


    @Column(name = "movieId")
    private String movieId;


    @Column(name = "category")
    private String category;


    @Column(name = "characters")
    private String characters;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "starId", insertable = false, updatable = false)
    private Star star;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieId", insertable = false, updatable = false)
    @JsonBackReference
    private Movie movie;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonView(Views.Public.class)
    public String getStarId() {
        return starId;
    }

    public void setStarId(String id) {
        this.starId = starId;
    }

    @JsonView(Views.Summary.class)
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {this.movieId = movieId;}

    @JsonView({Views.Public.class,Views.Summary.class})
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {this.category = category;}

    @JsonView({Views.Public.class,Views.Summary.class})
    public String getCharacters() {
        return characters;
    }

    public void setCharacters(String characters) {this.characters = characters;}

    @JsonView(Views.Public.class)
    public String getName() {return star.getName();}

    @JsonView(Views.Public.class)
    public String getPhoto() {return star.getPhoto();}


}