package com.moviedb_api.checkout;

import org.springframework.beans.factory.annotation.*;

public class ChargeRequest {

    public int getAmount() {
        return this.amount;
    }

    public Currency getCurrency() {
        return this.currency;
    }
    public String getDescription() {

        return this.description;
    }

    public String getStripeToken() {
        return this.stripeToken;
    }

    public void setAmount(Double amount) {
         this.amount = (int) (amount * 100);
    }

    public void setCurrency(Currency currency) {
         this.currency = currency;
    }
    public void setDescription(String description) {

         this.description = description;
    }

    public void setStripeToken(String token) {
         this.stripeToken = token;
    }

    public enum Currency {
        EUR, USD;
    }

    private String description;
    private int amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;
}