package com.moviedb_api.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class CustomerRequest {

    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Integer primaryAddress;

    public CustomerRequest() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {this.firstname = firstname;}

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {this.lastname = lastname;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {this.password = password;}

    public Integer getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Integer primaryAddress) {this.primaryAddress = primaryAddress;}
}