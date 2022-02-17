package com.moviedb_api.shipping;

import com.moviedb_api.sale.Sale;

import javax.persistence.*;

@Entity(name="Shipping")
@Table(name = "shipping")
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "customerId")
    private int customerId;

    @Column(name = "orderId")
    private int orderId;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "street")
    private String street;

    @Column(name = "unit")
    private String unit;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postcode")
    private String postcode;

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "orderId", insertable = false, updatable = false)
    private Sale sale;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Integer getOrderId() {return orderId;}

    public void setOrderId(Integer orderId) {this.orderId = orderId;}

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