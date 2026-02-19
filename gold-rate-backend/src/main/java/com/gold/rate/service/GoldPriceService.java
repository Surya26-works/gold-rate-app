package com.gold.rate.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GoldPriceService {

    @Value("${metalprice.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private Map<String, Object> getApiResponse() {
        return restTemplate.getForObject(apiUrl, Map.class);
    }

    private Map<String, Double> getRates() {
        Map<String, Object> response = getApiResponse();
        return (Map<String, Double>) response.get("rates");
    }

    // ================= GOLD =================

    public double getGoldPricePerOunceUSD() {
        Map<String, Double> rates = getRates();

        // API returns: 1 USD = X XAU
        // We need: 1 XAU = ? USD → so invert
        return 1 / rates.get("XAU");
    }

    public double getGoldPricePerGramINR() {
        double xauUsd = getGoldPricePerOunceUSD();
        double usdInr = getUsdToInr();

        // USD → INR (per ounce)
        double inrPerOunce = xauUsd * usdInr;

        // Add 6% customs
        double afterCustoms = inrPerOunce * 1.06;

        // Add 3% GST
        double afterGst = afterCustoms * 1.03;

        // Convert ounce → gram
        return afterGst / 31.103;
    }

    // ================= SILVER =================

    public double getSilverPricePerOunceUSD() {
        Map<String, Double> rates = getRates();

        // Invert
        return 1 / rates.get("XAG");
    }

    public double getSilverPricePerGramINR() {
        double xagUsd = getSilverPricePerOunceUSD();
        double usdInr = getUsdToInr();

        double inrPerOunce = xagUsd * usdInr;

        double afterCustoms = inrPerOunce * 1.06;
        double afterGst = afterCustoms * 1.03;

        return afterGst / 31.103;
    }

    // ================= USD INR =================

    public double getUsdToInr() {
        Map<String, Double> rates = getRates();
        return rates.get("INR");
    }
}