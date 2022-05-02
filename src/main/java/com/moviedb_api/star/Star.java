package com.moviedb_api.star;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.moviedb_api.Views;
import com.moviedb_api.cast.Star_Movie;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "stars")
public class Star {
    @Id
    @Column(name = "id")
    @JsonView(Views.Summary.class)
    private String starId;

    @Column(name = "name")
    @JsonView(Views.Summary.class)
    private String name;

    @Column(name = "birthYear")
    @JsonView(Views.Summary.class)
    private Integer birthYear;

    @Column(name = "photo")
    @JsonView(Views.Summary.class)
    private String photo;

    @Column(name = "bio")
    @JsonView(Views.Summary.class)
    private String bio;

    @Column(name = "birthName")
    @JsonView(Views.Summary.class)
    private String birthName;

    @Column(name = "birthDetails")
    @JsonView(Views.Summary.class)
    private String birthDetails;

    @Column(name = "dob")
    @JsonView(Views.Summary.class)
    private String dob;

    @Column(name = "place_of_birth")
    @JsonView(Views.Summary.class)
    private String place_of_birth;

    @Column(name = "dod")
    @JsonView(Views.Summary.class)
    private String dod;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "star")
    @JsonView(Views.Summary.class)
    private List<Star_Movie> movies;

    public String getStarId() {
        return starId;
    }

    public void setStarId(String id) {
        this.starId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {this.name = name;}

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {this.birthYear = birthYear;}

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {this.photo = photo;}

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {this.bio = bio;}

    public String getBirthName() {
        return birthName;
    }

    public void setBirthName(String birthName) {this.birthName = birthName;}

    public String getBirthDetails() {
        return birthDetails;
    }

    public void setBirthDetails(String birthDetails) {this.birthDetails = birthDetails;}

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {this.dob = dob;}

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public void setPlace_of_birth(String place_of_birth) {this.place_of_birth = place_of_birth;}

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {this.dod = dod;}

    public List<Star_Movie> getMovies() {return this.movies;}

    public void setMovies(Star_Movie movie) {movies.add(movie);}

}