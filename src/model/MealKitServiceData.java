package com.example.webcrawler.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MealKitServiceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String weeklyCost;
    private String mealOptions;
    private String dietaryPreferences;
    private String deliveryFrequency;
    private String ingredientQuality;

    // Constructors
    public MealKitServiceData() {
    }

    public MealKitServiceData(String name, String weeklyCost, String mealOptions,
                              String dietaryPreferences, String deliveryFrequency, String ingredientQuality) {
        this.name = name;
        this.weeklyCost = weeklyCost;
        this.mealOptions = mealOptions;
        this.dietaryPreferences = dietaryPreferences;
        this.deliveryFrequency = deliveryFrequency;
        this.ingredientQuality = ingredientQuality;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeeklyCost() {
        return weeklyCost;
    }

    public void setWeeklyCost(String weeklyCost) {
        this.weeklyCost = weeklyCost;
    }

    public String getMealOptions() {
        return mealOptions;
    }

    public void setMealOptions(String mealOptions) {
        this.mealOptions = mealOptions;
    }

    public String getDietaryPreferences() {
        return dietaryPreferences;
    }

    public void setDietaryPreferences(String dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences;
    }

    public String getDeliveryFrequency() {
        return deliveryFrequency;
    }

    public void setDeliveryFrequency(String deliveryFrequency) {
        this.deliveryFrequency = deliveryFrequency;
    }

    public String getIngredientQuality() {
        return ingredientQuality;
    }

    public void setIngredientQuality(String ingredientQuality) {
        this.ingredientQuality = ingredientQuality;
    }

    @Override
    public String toString() {
        return "org.example.MealKitServiceData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weeklyCost='" + weeklyCost + '\'' +
                ", mealOptions='" + mealOptions + '\'' +
                ", dietaryPreferences='" + dietaryPreferences + '\'' +
                ", deliveryFrequency='" + deliveryFrequency + '\'' +
                ", ingredientQuality='" + ingredientQuality + '\'' +
                '}';
    }
}
