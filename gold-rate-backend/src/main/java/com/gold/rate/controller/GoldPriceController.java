package com.gold.rate.controller;

import com.gold.rate.service.GoldPriceService;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")   // ✅ THIS IS THE FIX
@RequestMapping("/api/gold/price")
public class GoldPriceController {

    private final GoldPriceService goldPriceService;

    public GoldPriceController(GoldPriceService goldPriceService) {
        this.goldPriceService = goldPriceService;
    }

    @GetMapping("/details")
    public Map<String, Double> getGoldPriceDetails() {

        Map<String, Double> response = new HashMap<>();

        double xauUsd = goldPriceService.getGoldPricePerOunceUSD();
        double usdInr = goldPriceService.getUsdToInr();
        double pricePerGramInr = goldPriceService.getGoldPricePerGramINR();

        response.put("xauUsd", xauUsd);
        response.put("usdInr", usdInr);
        response.put("pricePerGramInr", pricePerGramInr);

        return response;
    }
}