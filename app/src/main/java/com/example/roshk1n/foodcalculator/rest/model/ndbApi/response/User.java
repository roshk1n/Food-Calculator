package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import com.example.roshk1n.foodcalculator.realm.DayRealm;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.ReminderReaml;
import com.example.roshk1n.foodcalculator.realm.UserRealm;

import java.util.ArrayList;

/**
 * Created by roshk1n on 8/13/2016.
 */
public class User {
    private String email;
    private String fullname;
    private String password;
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
        setPassword(userRealm.getPassword());
        setPhotoUrl(userRealm.getPhotoUrl());
        setAge(userRealm.getAge());
        setWeight(userRealm.getWeight());
        setHeight(userRealm.getHeight());
        setGoalCalories(userRealm.getGoalCalories());
        setSex(userRealm.getSex());
        setActiveLevel(userRealm.getActiveLevel());

        for (FoodRealm foodRealm : userRealm.getFavoriteList().getFoods()) {
            favoriteFood.add(new Food(foodRealm));
        }

        for (DayRealm dayRealm : userRealm.getDayRealms()) {
            days.add(new Day(dayRealm));
        }

        for (ReminderReaml reminderReaml : userRealm.getReminders()) {
            reminders.add(new Reminder(reminderReaml));
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
