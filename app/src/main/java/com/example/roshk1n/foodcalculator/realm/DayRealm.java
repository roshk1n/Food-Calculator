package com.example.roshk1n.foodcalculator.realm;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by roshk1n on 7/23/2016.
 */
public class DayRealm extends RealmObject {

    private Date date;
    private int goalCalories;
    private int eatDailyCalories;
    private int remainingCalories;
    private RealmList<FoodRealm> foods = new RealmList<FoodRealm>();

    public DayRealm() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getGoalCalories() {
        return goalCalories;
    }

    public void setGoalCalories(int limit_calories) {
        this.goalCalories = limit_calories;
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
