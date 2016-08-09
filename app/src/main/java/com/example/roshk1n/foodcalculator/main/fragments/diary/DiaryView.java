package com.example.roshk1n.foodcalculator.main.fragments.diary;

import com.example.roshk1n.foodcalculator.realm.FoodRealm;

public interface DiaryView {

    void updateCalories(String eat,String remaining, int checkLimit, int color);

    void setGoalCalories(String goalCalories);

    void makeSnackBar(FoodRealm deleteFood, int indexRemoved);
}
