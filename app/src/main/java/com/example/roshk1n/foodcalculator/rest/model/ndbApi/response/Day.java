package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import com.example.roshk1n.foodcalculator.realmModel.DayRealm;
import com.example.roshk1n.foodcalculator.realmModel.FoodRealm;
import com.example.roshk1n.foodcalculator.remoteDB.model.DayFirebase;
import com.example.roshk1n.foodcalculator.remoteDB.model.FoodFirebase;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
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

    public Day (DayFirebase dayFirebase) {
        setDate(dayFirebase.getDate());
        ArrayList<Food> foodArray = new ArrayList<>();
        for (FoodFirebase foodFirebase : dayFirebase.getFoods()) {
            foodArray.add(new Food(foodFirebase));
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> foodMap = new HashMap<>();
        HashMap<String, Object> foodw = new HashMap<>();

        for (Food food : foods) {
            foodMap.put("name", food.getName());
            foodMap.put("time", food.getTime());
            foodMap.put("ndbno", food.getNutrients());
            foodMap.put("portion", food.getPortion());
            foodMap.put("nutrients", food.getNutrients());
            foodw.put(food.getTime()+"",foodMap);
        }

        result.put("date", date);
        result.put("eatDailyCalories", eatDailyCalories);
        result.put("remainingCalories", remainingCalories);
        result.put("foods",foodw);

        return result;
    }
}
