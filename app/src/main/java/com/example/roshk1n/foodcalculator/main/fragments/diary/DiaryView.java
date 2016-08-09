package com.example.roshk1n.foodcalculator.main.fragments.diary;

public interface DiaryView {

    void updateCalories(String eat,String remaining, int checkLimit, int color);

    void setGoalCalories(String goalCalories);
}
