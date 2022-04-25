package com.moviedb_api.refreshToken;

import com.moviedb_api.customer.Customer;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Customer user;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false)
    private Instant expiration;


    //getters and setters
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Customer getUser() {
        return user;
    }
    public void setUser(Customer user) {
        this.user = user;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Instant getExpiration() {
        return expiration;
    }
    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

}