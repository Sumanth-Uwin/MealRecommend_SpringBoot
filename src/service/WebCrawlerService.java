package com.example.webcrawler.service;

import com.example.webcrawler.model.MealKitServiceData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebCrawlerService {

    public MealKitServiceData crawlHelloFresh() throws IOException {
        String url = "https://www.hellofresh.com/";
        Document doc = Jsoup.connect(url).get();

        MealKitServiceData data = new MealKitServiceData();
        data.setName("HelloFresh");

        // Example selectors - replace with actual ones from the site
        Element costElement = doc.selectFirst(".weekly-cost");
        Element mealOptionsElement = doc.selectFirst(".meal-options");

        if (costElement != null) data.setWeeklyCost(costElement.text());
        if (mealOptionsElement != null) data.setMealOptions(mealOptionsElement.text());

        return data;
    }

    public MealKitServiceData crawlBlueApron() throws IOException {
        String url = "https://www.blueapron.com/";
        Document doc = Jsoup.connect(url).get();

        MealKitServiceData data = new MealKitServiceData();
        data.setName("Blue Apron");

        // Example selectors - replace with actual ones from the site
        Element costElement = doc.selectFirst(".weekly-cost");
        Element mealOptionsElement = doc.selectFirst(".meal-options");

        if (costElement != null) data.setWeeklyCost(costElement.text());
        if (mealOptionsElement != null) data.setMealOptions(mealOptionsElement.text());

        return data;
    }

    // Add additional methods for other services here...

    public List<MealKitServiceData> crawlAllServices() throws IOException {
        List<MealKitServiceData> services = new ArrayList<>();
        services.add(crawlHelloFresh());
        services.add(crawlBlueApron());
        // Add other service crawl methods here
        return services;
    }
}
