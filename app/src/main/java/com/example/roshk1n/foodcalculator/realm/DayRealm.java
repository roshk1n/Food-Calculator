package com.example.roshk1n.foodcalculator.realm;

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

    public Day convertToModel() {
        Day day = new Day();
        day.setDate(this.getDate());
        for (int i = 0; i<this.getFoods().size(); i++) {
            day.getFoods().add(this.getFoods().get(i).converToModel());
        }
        day.setEatDailyCalories(this.getEatDailyCalories());
        day.setRemainingCalories(this.getRemainingCalories());

        return day;
    }
}
