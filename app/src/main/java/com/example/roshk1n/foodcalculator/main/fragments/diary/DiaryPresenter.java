package com.example.roshk1n.foodcalculator.main.fragments.diary;

import android.support.v4.app.Fragment;

import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;

import java.util.Date;

import io.realm.RealmList;

/**
 * Created by roshk1n on 7/19/2016.
 */

public interface DiaryPresenter  {

    void setView(DiaryView view);

    void showDatePicker(Fragment fragment);

    UserRealm getCurrentUserRealm();

    RealmList<FoodRealm> getFoods(UserRealm user, Date date);

    void removeFood(int index, Date date);

}
