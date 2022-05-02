package com.moviedb_api.cast;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.moviedb_api.Views;
import com.moviedb_api.movie.Movie;
import com.moviedb_api.movie.MovieSimplified;
import com.moviedb_api.star.Star;

import javax.persistence.*;

@Entity
@Table(name = "stars_in_movies")
public class Star_Movie  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @JsonView({Views.Summary.class})
    private int id;

    @Column(name = "starId")
    @JsonView({Views.Public.class,Views.Summary.class})
    private String starId;

    @Column(name = "movieId")
    @JsonView({Views.Summary.class})
    private String movieId;


    @Column(name = "category")
    @JsonView({Views.Public.class,Views.Summary.class})
    private String category;


    @Column(name = "characters")
    @JsonView({Views.Public.class,Views.Summary.class})
    private String characters;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "starId", insertable = false, updatable = false)
    @JsonBackReference
    private Star star;

    @ManyToOne
    @JoinColumn(name = "movieId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonView(Views.Summary.class)
    private MovieSimplified movie;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStarId() {
        return starId;
    }

    public void setStarId(String id) {
        this.starId = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {this.category = category;}

    public String getCharacters() {
        return characters;
    }

    public void setCharacters(String characters) {this.characters = characters;}

    @JsonView(Views.Public.class)
    public String getName() {return star.getName();}

    @JsonView(Views.Public.class)
    public String getPhoto() {return star.getPhoto();}

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {this.movieId = movieId;}

    public MovieSimplified getMovie() { return movie; }

    public void setMovie(MovieSimplified movie) {this.movie = movie;}
}