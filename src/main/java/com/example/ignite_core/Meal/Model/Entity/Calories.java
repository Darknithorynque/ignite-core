package com.example.ignite_core.Meal.Model.Entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class Calories implements Serializable {

    private double totalCalories;

    public Calories(double totalCalories, double proteins, double fats, double carbohydrates, double sugars) {
        this.totalCalories = totalCalories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.sugars = sugars;
    }

    public Calories() {}

    public double getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(double totalCalories) {
        this.totalCalories = totalCalories;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getSugars() {
        return sugars;
    }

    public void setSugars(double sugars) {
        this.sugars = sugars;
    }

    private double proteins;

    private double fats;

    private double carbohydrates;

    private double sugars;
}
