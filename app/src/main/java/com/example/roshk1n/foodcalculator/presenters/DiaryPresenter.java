package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.views.DiaryView;

public interface DiaryPresenter  {

    void setView(DiaryView view);

    String getDateString();

    void removeFoodDB(int index, long time);

    void calculateCalories();

    void getGoalCalories();

    void loadDay();

    void setFollowDate();

    void setNextDate();

    void destroy();
}
