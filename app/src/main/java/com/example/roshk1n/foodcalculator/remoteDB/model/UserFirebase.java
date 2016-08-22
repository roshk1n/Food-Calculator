package com.example.roshk1n.foodcalculator.remoteDB.model;


import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User;

import java.util.ArrayList;

public class UserFirebase {
    private Long age;
    private Long weight;
    private Long height;
    private Long goalCalories;
    private String sex;
    private String activeLevel;

    private ArrayList<FoodFirebase> favoriteFood = new ArrayList<>();
    private ArrayList<ReminderFirebase> reminders = new ArrayList<>();
    private ArrayList<DayFirebase> days = new ArrayList<>();

    public UserFirebase() {}

    public UserFirebase (User user) {
        setAge((long)user.getAge());
        setWeight((long)user.getWeight());
        setHeight((long)user.getHeight());
        setGoalCalories((long)user.getGoalCalories());
        setSex(user.getSex());
        setActiveLevel(user.getActiveLevel());
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Long getGoalCalories() {
        return goalCalories;
    }

    public void setGoalCalories(Long goalCalories) {
        this.goalCalories = goalCalories;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getActiveLevel() {
        return activeLevel;
    }

    public void setActiveLevel(String activeLevel) {
        this.activeLevel = activeLevel;
    }

    public ArrayList<FoodFirebase> getFavoriteFood() {
        return favoriteFood;
    }

    public void setFavoriteFood(ArrayList<FoodFirebase> favoriteFood) {
        this.favoriteFood = favoriteFood;
    }

    public ArrayList<ReminderFirebase> getReminders() {
        return reminders;
    }

    public void setReminders(ArrayList<ReminderFirebase> reminders) {
        this.reminders = reminders;
    }

    public ArrayList<DayFirebase> getDays() {
        return days;
    }

    public void setDays(ArrayList<DayFirebase> days) {
        this.days = days;
    }
}
