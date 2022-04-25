package com.moviedb_api.movie;

import com.fasterxml.jackson.annotation.JsonView;
import com.moviedb_api.Views;
import com.moviedb_api.inventory.Inventory;
import com.moviedb_api.price.Price;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Entity
@Table(name = "movies")
public class MovieSimplified {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name="title")
    private String title;

    @Column(name="year")
    private int year;

    @Column(name="director")
    private String director;

    @Column(name="poster")
    private String poster;

    @Column(name="plot")
    private String plot;

    @Column(name="rated")
    private String rated;

    @Column(name="runtime")
    private String runtime;

    @Column(name="background")
    private String background;

    @Column(name = "updated")
    private Date updated;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "movieid")
    private Price price;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "productId")
    private Inventory inventory;

    public MovieSimplified() {
    }

    public MovieSimplified(String id, String title, int year, String director, String poster, String plot, String rated, String runtime, String background) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.poster = poster;
        this.plot = plot;
        this.rated = rated;
        this.runtime = runtime;
        this.background = background;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public String getPoster() {
        return poster;
    }

    public String getPlot() {
        return plot;
    }

    public String getRated() {
        return rated;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getBackground() {
        return background;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Double getPrice() {return price.getPrice(); }

    public void setPrice(Price price) { this.price = price; }

    public Map<String, Object> getInventory() {
        Map<String, Object> map = new HashMap<>();
        map.put("quantity", inventory.getQuantity());
        map.put("status", inventory.getStatus());
        return map;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
