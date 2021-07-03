package com.moviedb_api.checkout;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class CheckoutController {

    @Value("${stripe.public.key}")
    private String stripePublicKey;

    @RequestMapping("/checkout")
    public String checkout(
        @RequestParam Double amount,
        @RequestParam String key,
        @RequestParam String currency
    ) {

        Map<String,Object> model = new HashMap<String, Object>();

        model.put("amount", amount*100); // in cents
        model.put("stripePublicKey", key);

        if(currency.equals("USD")) {
            model.put("currency", ChargeRequest.Currency.USD);
        }
        else {
            model.put("currency", ChargeRequest.Currency.EUR);
        }

        ChargeRequest request = new ChargeRequest();
        request.setAmount(amount*100);
        request.setCurrency(ChargeRequest.Currency.USD);
        request.setStripeToken(key);
        request.setDescription("Test Charge");

        return model.toString();
    }
}