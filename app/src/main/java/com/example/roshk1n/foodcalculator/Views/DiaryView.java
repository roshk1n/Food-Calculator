package com.example.roshk1n.foodcalculator.Views;

public interface DiaryView {

    void updateCalories(String eat,String remaining, int color);

    void setGoalCalories(String goalCalories);

    void showDialog(String remainingCalories, int checkLimit);

    void showHintAddAnim();

    void hideHintAddAnim();
}
