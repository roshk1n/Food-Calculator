package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import java.util.ArrayList;

public class Day {
    private long date;
    private int eatDailyCalories;
    private int remainingCalories;

    ArrayList<Food> foods = new ArrayList<>();

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getEatDailyCalories() {
        return eatDailyCalories;
    }

    public void setEatDailyCalories(int eatDailyCalories) {
        this.eatDailyCalories = eatDailyCalories;
    }

    public int getRemainingCalories() {
        return remainingCalories;
    }

    public void setRemainingCalories(int remainingCalories) {
        this.remainingCalories = remainingCalories;
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }
}
