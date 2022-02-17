package com.moviedb_api.sale;

import com.moviedb_api.shipping.Shipping;
import com.moviedb_api.shipping.ShippingRequest;
import org.json.JSONObject;

import java.sql.Date;

public class SaleRequest {
    private Integer id;
    private Integer customerId;
    private Date saleDate;
    private Double salesTax;
    private Double subTotal;
    private Double total;
    private String stripeId;
    private String device;

    private ShippingRequest shipping;

    public  SaleRequest() {}

    public Integer getId() {
        return id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public Double getSalesTax() {
        return salesTax;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public Double getTotal() {
        return total;
    }

    public String getStripeId() {return stripeId;}

    public ShippingRequest getShipping() {return shipping;}

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public void setSalesTax(Double salesTax) {
        this.salesTax = salesTax;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setShipping(ShippingRequest shipping) {
        this.shipping = shipping;
    }

    public void setStripeId(String stripeId) {this.stripeId = stripeId;}

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
