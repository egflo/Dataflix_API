package com.moviedb_api.order;

import com.moviedb_api.movie.Movie;
import com.moviedb_api.movie.MovieSimplified;
import com.moviedb_api.sale.Sale;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "orderId")
    private int orderId;

    @Column(name = "movieId")
    private String movieId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "listPrice")
    private Double listPrice;

    @ManyToOne
    @JoinColumn(name = "movieId", referencedColumnName = "id", insertable = false, updatable = false)
    private MovieSimplified movie;

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "orderId", referencedColumnName = "id", insertable = false, updatable = false)
    private Sale sale;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    /**
    public String getTitle() {return movie.getTitle();}

    public String getPoster() {return movie.getPoster();}

    public int getYear() {return movie.getYear();}

    public String gerRunTime() {return movie.getRuntime();}

    public String getRated() {return movie.getRated();}

     **/

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {this.quantity = quantity;}

    public Double getListPrice() {
        return listPrice;
    }

    public void setListPrice(Double listPrice) {this.listPrice = listPrice;}

    public MovieSimplified getMovie() {
        return movie;
    }

    public void setMovie(MovieSimplified movie) {
        this.movie = movie;
    }

}