package com.example.roshk1n.foodcalculator.views;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;

public interface DiaryView {
    void updateCalories(String eat,String remaining, int color);

    void setGoalCalories(String goalCalories);

    void showDialog(String remainingCalories, int checkLimit);

    void showHintAddAnim();

    void hideHintAddAnim();

    void setDay(Day day);
}
