package com.moviedb_api.user_address;

import com.fasterxml.jackson.annotation.JsonView;
import com.moviedb_api.Views;
import com.moviedb_api.genre.Genre;
import com.moviedb_api.movie.Movie;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;


@Entity
@Table(name = "users_addresses")
public class User_Address {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "userId")
    private int userId;

    @Column(name = "addressId")
    private int addressId;

    public int getId(){return id;}

    public void setId(Integer id){this.id = id;}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

}