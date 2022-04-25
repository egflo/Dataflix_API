package com.moviedb_api.shipping;

import javax.persistence.Column;
import java.sql.Date;

public class ShippingRequest {
    private Integer id;
    private Integer customerId;
    private Integer orderId;

    private String firstname;
    private String lastname;
    private String street;
    private String unit;
    private String city;
    private String state;
    private String postcode;

    public ShippingRequest() {}

    public Integer getId() {
        return id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public String getFirstname() { return firstname; }

    public String getLastname() {
        return lastname;
    }

    public String getStreet() {
        return street;
    }

    public String getUnit() {
        return unit;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostcode() {
        return postcode;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setOrderId(Integer orderId) {this.orderId = orderId;}

    public void setFirstname(String firstname) {this.firstname = firstname;}

    public void setLastname(String lastname) {this.lastname = lastname;}

    public void setStreet(String street) {this.street = street;}

    public void setUnit(String unit) {this.unit = unit;}

    public void setCity(String city) {this.city = city;}

    public void setState(String state) {this.state = state;}

    public void setPostcode(String postcode) {this.postcode = postcode;}
}
