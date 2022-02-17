package com.moviedb_api.sale;

import com.moviedb_api.order.Order;
import com.moviedb_api.shipping.Shipping;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "customerId")
    private int customerId;

    @Column(name = "saleDate")
    private Date saleDate;

    @Column(name = "salesTax")
    private Double salesTax;

    @Column(name = "subTotal")
    private Double subTotal;

    @Column(name = "total")
    private Double total;

    @Column(name = "stripeId")
    private String stripeId;

    @Column(name = "status")
    private String status;

    @Column(name = "device")
    private String device;

    //@OneToMany(targetEntity= Order.class, mappedBy = "sale", fetch = FetchType.LAZY)
    //@ManyToMany(targetEntity= Order.class, mappedBy = "orderId", fetch = FetchType.LAZY)
   // @OneToMany(mappedBy="sale", fetch = FetchType.LAZY)
    @OneToMany(fetch = FetchType.EAGER,mappedBy="sale",cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<Order>();

    //@OneToOne(mappedBy = "sale")
    //@OneToOne(targetEntity= Shipping.class, mappedBy = "sale", fetch = FetchType.LAZY)
    @OneToOne(fetch = FetchType.EAGER,mappedBy="sale",cascade = CascadeType.ALL)
    private Shipping shipping;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {this.saleDate = saleDate;}

    public Double getSalesTax() { return salesTax;}

    public void setSalesTax(Double salesTax) {this.salesTax = salesTax;}

    public Double getSubTotal() { return subTotal;}

    public void setSubTotal(Double subTotal) {this.subTotal = subTotal;}

    public Double getTotal() { return total;}

    public void setTotal(Double total) {this.total = total;}

    public List<Order> getOrders() {return orders; }

    public void setOrders(Order order) {this.orders.add(order); }

    public Shipping getShipping() {return shipping; }

    public void setShipping(Shipping shipping) {this.shipping = shipping; }

    public String getStripeId() {return stripeId;}

    public void setStripeId(String stripeId) {this.stripeId = stripeId;}

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}

    public String getDevice() {return device;}

    public void setDevice(String device) {this.device = device;}

}