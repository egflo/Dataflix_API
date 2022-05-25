package com.moviedb_api.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moviedb_api.address.Address;
import com.moviedb_api.review.Review;
import com.moviedb_api.roles.Role;
import com.moviedb_api.sale.Sale;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "customers")
public class Customer implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "primaryAddress")
    private int primaryAddress;

    @Column(name = "created")
    private Date created;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    @JoinColumn(name = "customerid")
    private List<Sale> sales = new ArrayList<Sale>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    @JoinColumn(name = "customerid")
    private List<Review> reviews = new ArrayList<Review>();


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_addresses",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "addressId")
    )
    private List<Address> addresses = new ArrayList<>();

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {this.email = email;}

    public int getPrimaryAddress() {return primaryAddress;}

    public void setPrimaryAddress(int primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    //public  List<Review> getReviews() {return reviews;}

    ///public  void setReviews(Review review) {reviews.add(review);}

    //public  List<Sale> getSales() {return sales;}

    /// public  void setSales(Sale sale) {sales.add(sale);}

    public  List<Address> getAddresses() {return addresses;}

    public  void setAddresses(Address address) {addresses.add(address);}

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {this.password = password;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer user = (Customer) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email, password, created);
    }
}

