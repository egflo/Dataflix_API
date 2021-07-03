package com.moviedb_api.checkout;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.ApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.google.gson.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ChargeController {

    @Autowired
    private StripeService paymentsService;

    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    @ResponseBody
    public String charge(
        @RequestParam Double amount,
        @RequestParam String key,
        @RequestParam String currency
    ) throws StripeException
    {
        ChargeRequest chargeRequest = new ChargeRequest();
        chargeRequest.setAmount(amount);
        chargeRequest.setStripeToken(key);
        if(currency.equals("USD")) {
            chargeRequest.setCurrency(ChargeRequest.Currency.USD);
        }
        else {
            chargeRequest.setCurrency(ChargeRequest.Currency.EUR);
        }

        chargeRequest.setDescription("Example charge");
        Charge charge = paymentsService.createCharge(chargeRequest);


        Gson gson = new Gson();
        JsonObject object =  gson.fromJson(charge.toJson(), JsonObject.class);

       // System.out.println(charge.toJson());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("receipt_url", object.get("receipt_url").getAsString());
        params.put("status", object.get("status").getAsString());
        params.put("currency", object.get("currency").getAsString());
        params.put("amount", object.get("amount").getAsInt());
        params.put("id", object.get("id").getAsString());


        System.out.println(object.get("payment_method_details"));

        JsonObject payment_method_details = object.get("payment_method_details").getAsJsonObject();
        JsonObject card_details = payment_method_details.get("card").getAsJsonObject();

        params.put("network", card_details.get("brand").getAsString());
        params.put("last4", card_details.get("last4").getAsString());
        params.put("exp_year", card_details.get("exp_year").getAsString());
        params.put("exp_month", card_details.get("exp_month").getAsString());

        System.out.println(charge.toJson());

        return gson.toJson(params);
    }

    @ExceptionHandler(StripeException.class)
    @ResponseBody
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return model.toString();
    }
}