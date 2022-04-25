package com.moviedb_api.checkout;

import org.springframework.beans.factory.annotation.*;

public class ChargeRequest {

    public Double getAmount() {
        return this.amount;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public String getDescription() {
        return this.description;
    }

    public String getToken() {
        return this.token;
    }

    public void setAmount(Double amount) {
         this.amount = amount;
    }

    public void setCurrency(Currency currency) {
         this.currency = currency;
    }

    public void setDescription(String description) {
         this.description = description;
    }

    public void setToken(String token) {
         this.token = token;
    }

    public enum Currency {
        EUR, USD;
    }

    private String description;
    private Double amount;
    private Currency currency;
    private String stripeEmail;
    private String token;
}