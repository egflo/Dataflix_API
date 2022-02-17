package com.moviedb_api.genre;

import com.fasterxml.jackson.annotation.JsonView;
import com.moviedb_api.Views;
import com.moviedb_api.genres_in_movies.Genre_Movie;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonView(Views.Public.class)
    private int id;

    @Column(name = "name")
    @JsonView(Views.Public.class)
    private String name;

    //@OneToOne(mappedBy = "genre")
    //private Genre_Movie genre;

    @OneToMany(mappedBy = "genre")
    private List<Genre_Movie> genre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {this.name = name;}
}