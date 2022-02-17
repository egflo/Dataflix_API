package com.moviedb_api.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moviedb_api.review.Review;
import com.moviedb_api.roles.Role;
import com.moviedb_api.sale.Sale;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "firstName")
    private String firstname;

    @Column(name = "lastName")
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstName) {this.firstname = firstName;}

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

    // Overriding equals() to compare two Complex objects
    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Address)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Address c = (Address) o;

        // Compare the data members and return accordingly
        return Double.compare(id, c.id) == 0;
    }

}
