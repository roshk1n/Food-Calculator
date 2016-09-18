package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import com.example.roshk1n.foodcalculator.realmModel.DayRealm;
import com.example.roshk1n.foodcalculator.realmModel.ReminderReaml;
import com.example.roshk1n.foodcalculator.realmModel.UserRealm;
import com.example.roshk1n.foodcalculator.remoteDB.model.DayFirebase;
import com.example.roshk1n.foodcalculator.remoteDB.model.FoodFirebase;
import com.example.roshk1n.foodcalculator.remoteDB.model.ReminderFirebase;
import com.example.roshk1n.foodcalculator.remoteDB.model.UserFirebase;
import java.util.ArrayList;

public class User {
    private String email;
    private String fullname;
    private String photoUrl;
    private int age;
    private int weight;
    private int height;
    private int goalCalories;
    private String sex;
    private String activeLevel;

    private ArrayList<Food> favoriteFood = new ArrayList<>();
    private ArrayList<Reminder> reminders = new ArrayList<>();
    private ArrayList<Day> days = new ArrayList<>();

    public User() {}

    public User(UserRealm userRealm) {
        setEmail(userRealm.getEmail());
        setFullname(userRealm.getFullname());
        setPhotoUrl(userRealm.getPhotoUrl());
        setAge(userRealm.getAge());
        setWeight(userRealm.getWeight());
        setHeight(userRealm.getHeight());
        setGoalCalories(userRealm.getGoalCalories());
        setSex(userRealm.getSex());
        setActiveLevel(userRealm.getActiveLevel());

        for (DayRealm dayRealm : userRealm.getDayRealms()) {
            days.add(new Day(dayRealm));
        }

        for (ReminderReaml reminderReaml : userRealm.getReminders()) {
            reminders.add(new Reminder(reminderReaml));
        }

    }

    public User(UserFirebase userFirebase) {
        setAge((int)(long)userFirebase.getAge());
        setWeight((int)(long)userFirebase.getWeight());
        setHeight((int)(long)userFirebase.getHeight());
        setGoalCalories((int)(long)userFirebase.getGoalCalories());
        setSex(userFirebase.getSex());
        setActiveLevel(userFirebase.getActiveLevel());

        for (FoodFirebase foodFirebase : userFirebase.getFavoriteFood()) {
            favoriteFood.add(new Food(foodFirebase));
        }

        for (DayFirebase dayFirebase : userFirebase.getDays()) {
            days.add(new Day(dayFirebase));
        }

        for (ReminderFirebase reminderFirebase : userFirebase.getReminders()) {
            reminders.add(new Reminder(reminderFirebase));
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getGoalCalories() {
        return goalCalories;
    }

    public void setGoalCalories(int goalCalories) {
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

    public ArrayList<Food> getFavoriteFood() {
        return favoriteFood;
    }

    public void setFavoriteFood(ArrayList<Food> favoriteFood) {
        this.favoriteFood = favoriteFood;
    }

    public ArrayList<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(ArrayList<Reminder> reminders) {
        this.reminders = reminders;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }
}
