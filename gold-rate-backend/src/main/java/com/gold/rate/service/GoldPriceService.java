package com.gold.rate.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gold.rate.model.MetalPriceResponse;

@Service
public class GoldPriceService {

    @Value("${metalprice.api.key}")
    private String apiKey;

    @Value("${metalprice.api.url}")
    private String apiUrl;

    private static final double GRAMS_PER_OUNCE = 31.1;

    private final RestTemplate restTemplate = new RestTemplate();

    private MetalPriceResponse fetchPrices() {
        String url = apiUrl
                + "?api_key=" + apiKey
                + "&base=USD"
                + "&currencies=XAU,INR";

        return restTemplate.getForObject(url, MetalPriceResponse.class);
    }

    // 🔹 XAU/USD (price of 1 ounce gold in USD)
    public double getGoldPricePerOunceUSD() {
        MetalPriceResponse response = fetchPrices();

        // API gives ounces per USD → invert it
        double ouncesPerUsd = response.getRates().get("XAU");
        return 1 / ouncesPerUsd;
    }

    // 🔹 USD → INR
    public double getUsdToInr() {
        MetalPriceResponse response = fetchPrices();
        return response.getRates().get("INR");
    }

    // 🔹 FINAL OWNER FORMULA
    // a = XAU/USD * USD/INR
    // b = a + 6%
    // c = b + 3%
    // d = c / 31.1
    public double getGoldPricePerGramINR() {

        double a = getGoldPricePerOunceUSD() * getUsdToInr();
        double b = a * 1.06;
        double c = b * 1.03;

        return c / GRAMS_PER_OUNCE;
    }
}