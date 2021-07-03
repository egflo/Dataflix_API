package com.moviedb_api.customer;

import com.moviedb_api.order.Order;
import com.moviedb_api.sale.Sale;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "address")
    private String address;

    @Column(name = "unit")
    private String unit;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postcode")
    private String postcode;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    @JoinColumn(name = "customerid")
    private List<Sale> sales = new ArrayList<Sale>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {this.address = address;}

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {this.unit = unit;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {this.password = password;}

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

    public  List<Sale> getSales() {return sales;}

    public  void setSales(Sale sale) {sales.add(sale);}

}
