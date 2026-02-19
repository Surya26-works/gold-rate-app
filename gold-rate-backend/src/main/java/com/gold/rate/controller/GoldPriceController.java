package com.gold.rate.controller;

import com.gold.rate.service.GoldPriceService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/gold/price")
public class GoldPriceController {

    private final GoldPriceService goldPriceService;

    public GoldPriceController(GoldPriceService goldPriceService) {
        this.goldPriceService = goldPriceService;
    }

    @GetMapping("/details")
    public Map<String, Double> getPriceDetails() {

        Map<String, Double> response = new HashMap<>();

        // GOLD
        double goldPerGram = goldPriceService.getGoldPricePerGramINR();
        double goldXauUsd = goldPriceService.getGoldPricePerOunceUSD();

        // SILVER
        double silverPerGram = goldPriceService.getSilverPricePerGramINR();
        double silverXagUsd = goldPriceService.getSilverPricePerOunceUSD();

        // USD INR
        double usdInr = goldPriceService.getUsdToInr();

        response.put("goldPricePerGramInr", goldPerGram);
        response.put("silverPricePerGramInr", silverPerGram);
        response.put("xauUsd", goldXauUsd);
        response.put("xagUsd", silverXagUsd);
        response.put("usdInr", usdInr);

        return response;
    }
}