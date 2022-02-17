package com.moviedb_api.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class PasswordRequest {

    private int id;
    private String password;
    private String newPassword;


    public PasswordRequest() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {this.password = password;}

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {this.newPassword = newPassword;}

}