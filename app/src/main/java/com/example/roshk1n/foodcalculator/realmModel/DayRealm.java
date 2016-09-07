package com.example.roshk1n.foodcalculator.realmModel;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import io.realm.RealmList;
import io.realm.RealmObject;

public class DayRealm extends RealmObject {

    private long date;
    private int eatDailyCalories;
    private int remainingCalories;
    private RealmList<FoodRealm> foods = new RealmList<FoodRealm>();

    public DayRealm() {
    }

    public DayRealm(Day day) {
        setDate(day.getDate());
        setEatDailyCalories(day.getEatDailyCalories());
        setRemainingCalories(day.getRemainingCalories());
        for (Food food : day.getFoods()) {
            foods.add(new FoodRealm(food));
        }
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

    public RealmList<FoodRealm> getFoods() {
        return foods;
    }

    public void setFoods(RealmList<FoodRealm> foods) {
        this.foods = foods;
    }
}
