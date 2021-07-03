package com.moviedb_api.checkout;

import com.stripe.Stripe;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public String createCustomer(String email, String token) {

        String id = null;

        try {
            Stripe.apiKey = secretKey;
            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("description", "Customer for " + email);
            customerParams.put("email", email);
            // obtained with stripe.js
            customerParams.put("source", token);

            Customer customer = Customer.create(customerParams);
            id = customer.getId();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public Charge createCharge(ChargeRequest chargeRequest)
            throws AuthenticationException, InvalidRequestException,
             CardException, StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());
        Charge charge = Charge.create(chargeParams);
        return charge;
    }
}
