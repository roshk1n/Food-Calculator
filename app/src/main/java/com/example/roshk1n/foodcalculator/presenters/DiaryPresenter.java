package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.Views.DiaryView;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;

import java.util.Date;

import io.realm.RealmList;

/**
 * Created by roshk1n on 7/19/2016.
 */

public interface DiaryPresenter  {

    void setView(DiaryView view);

    UserRealm getCurrentUserRealm();

    RealmList<FoodRealm> getFoods(Date date);

    String dateToString(Date date);

    void removeFood(int index, Date date);

    void calculateCalories(Date date);

    void getGoalCalories();
}
