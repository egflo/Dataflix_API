package com.moviedb_api.genres_in_movies;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.moviedb_api.Views;
import com.moviedb_api.genre.Genre;
import com.moviedb_api.movie.Movie;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;


@Entity
@Table(name = "genres_in_movies")
public class Genre_Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "genreId")
    private int genreId;

    @Column(name = "movieId")
    private String movieId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieId", insertable = false, updatable = false)
    @Fetch(FetchMode.JOIN)
    private Movie movie;

    @ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "genreId", referencedColumnName = "id")
    @JoinColumn(name = "genreId", referencedColumnName = "id", insertable = false, updatable = false)
    //@JsonBackReference
    private Genre genre;

    public int getId(){return id;}

    public void setId(Integer id){this.id = id;}

    @JsonView(Views.Public.class)
    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {this.movieId = movieId;}

    @JsonView(Views.Public.class)
    public String getName() {
        return genre.getName();
    }


    //public Movie getMovie() {return movie;}

   // public String getTitle() {return movie.getTitle();}

   // public String getPoster() {return movie.getPoster();}

    //public Integer getYear() {return movie.getYear();}

    //public String getRated() {return movie.getRated();}

    //public String getRuntime() {return movie.getRuntime();}



}