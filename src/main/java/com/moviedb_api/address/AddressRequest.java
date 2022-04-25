package com.moviedb_api.address;

public class AddressRequest {

    private int id;
    private int userId;
    private String firstname;
    private String lastname;
    private String street;
    private String unit;
    private String city;
    private String state;
    private String postcode;

    public AddressRequest() {}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {this.firstname = firstname;}

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {this.lastname = lastname;}

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {this.street = street;}

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {this.unit = unit;}

    public String getCity() {
        return city;
    }

    public void setCity(String city) {this.city = city;}

    public String getState() {
        return state;
    }

    public void setState(String state) {this.state = state;}

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {this.postcode = postcode;}
}