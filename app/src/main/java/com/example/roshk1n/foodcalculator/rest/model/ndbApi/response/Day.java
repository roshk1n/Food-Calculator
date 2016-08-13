package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import com.example.roshk1n.foodcalculator.realm.DayRealm;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;

import java.util.ArrayList;

public class Day {
    private long date;
    private int eatDailyCalories;
    private int remainingCalories;
    private ArrayList<Food> foods = new ArrayList<>();

    public Day(){}

    public Day(DayRealm dayRealm) {
        setDate(dayRealm.getDate());
        setEatDailyCalories(dayRealm.getEatDailyCalories());
        setRemainingCalories(dayRealm.getRemainingCalories());
        ArrayList <Food> foodArray = new ArrayList<>();
        for (FoodRealm foodRealm : dayRealm.getFoods()) {
            foodArray.add(new Food(foodRealm));
        }
        setFoods(foodArray);
    }

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
