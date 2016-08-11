package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.Views.DiaryView;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmList;

/**
 * Created by roshk1n on 7/19/2016.
 */

public interface DiaryPresenter  {

    void setView(DiaryView view);

    String getDateString();

    void removeFoodDB(int index);

    void calculateCalories();

    void getGoalCalories();

    Day loadDay();

    void setFollowDate();

    void setNextDate();
}
