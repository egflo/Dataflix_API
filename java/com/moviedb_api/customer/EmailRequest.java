package com.moviedb_api.customer;

public class EmailRequest {

    private int id;
    private String password;
    private String email;
    private String newEmail;


    public EmailRequest() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {this.email = email;}

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {this.newEmail = newEmail;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {this.password = password;}

}