package com.example.roshk1n.foodcalculator.realmModel;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Reminder;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserRealm extends RealmObject {
    @PrimaryKey
    private String email;
    private String fullname;
    private String photoUrl;
    private int age;
    private int weight;
    private int height;
    private int goalCalories;
    private String sex;
    private String activeLevel;

    private RealmList<ReminderReaml> reminders = new RealmList<>();
    private RealmList<DayRealm> dayRealms = new RealmList<>();

    public UserRealm() { }

    public UserRealm(String fullname, String email, String photo ) {
        this.fullname = fullname;
        this.email = email;
        this.photoUrl = photo;
    }

    public UserRealm(User user) {
        setEmail(user.getEmail());
        setFullname(user.getFullname());
        setPhotoUrl(user.getPhotoUrl());
        setAge(user.getAge());
        setWeight(user.getWeight());
        setHeight(user.getHeight());
        setGoalCalories(user.getGoalCalories());
        setSex(user.getSex());
        setActiveLevel(user.getActiveLevel());

        for (Reminder reminder : user.getReminders()) {
            reminders.add(new ReminderReaml(reminder));
        }

        for (Day day : user.getDays()) {
            dayRealms.add(new DayRealm(day));
        }
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
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

    public String getActiveLevel() {
        return activeLevel;
    }

    public void setActiveLevel(String activeLevel) {
        this.activeLevel = activeLevel;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getGoalCalories() {
        return goalCalories;
    }

    public void setGoalCalories(int limit_calories) {
        this.goalCalories = limit_calories;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public RealmList<DayRealm> getDayRealms() {
        return dayRealms;
    }

    public void setDayRealms(RealmList<DayRealm> dayRealms) {
        this.dayRealms = dayRealms;
    }

    public RealmList<ReminderReaml> getReminders() {
        return reminders;
    }

    public void setReminders(RealmList<ReminderReaml> reminders) {
        this.reminders = reminders;
    }

}
