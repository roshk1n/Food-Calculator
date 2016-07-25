package com.example.roshk1n.foodcalculator.main.fragments.infoFood;

import android.util.Log;

import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.realm.DayRealm;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import io.realm.Realm;

/**
 * Created by roshk1n on 7/25/2016.
 */
public class InfoFoodPresenterImpl implements InfoFoodPresenter {

    private  Realm realm = Realm.getDefaultInstance();

    private InfoFoodView foodView;

    @Override
    public void setView(InfoFoodView view) {
        foodView = view;
    }

    @Override
    public void addNewFood(Food food) {

        FoodRealm foodRealm = food.converToRealm();
        UserRealm user = getCurrentUserRealm();
        Log.d("m",foodRealm.getTime().getDate()+"");
        for (int i = 0; i < user.getDayRealms().size(); i++) {
            if(user.getDayRealms().get(i).getDate().getDate()==foodRealm.getTime().getDate()
                    && user.getDayRealms().get(i).getDate().getMonth()==foodRealm.getTime().getMonth()
                    && user.getDayRealms().get(i).getDate().getYear()==foodRealm.getTime().getYear()) {

                realm.beginTransaction();
                user.getDayRealms().get(i).getFoods().add(foodRealm);
                realm.commitTransaction();
                break;
            }  else {
                if(i==user.getDayRealms().size()-1) {
                    realm.beginTransaction();
                    DayRealm newDay = new DayRealm();
                    newDay.setDate(foodRealm.getTime());
                    newDay.setGoalCalories(1600);
                    newDay.setEatDailyCalories(0);
                    newDay.setRemainingCalories(1600);
                    user.getDayRealms().add(newDay);
                    realm.commitTransaction();
                    realm.beginTransaction();
                    user.getDayRealms().get(i+1).getFoods().add(foodRealm);
                    realm.commitTransaction();
                    break;
                }
            }
        }
        if(user.getDayRealms().size()==0) { //if it`s first day for this user

            realm.beginTransaction();
            DayRealm newDay = new DayRealm();
            newDay.setDate(foodRealm.getTime());
            newDay.setGoalCalories(1600);
            newDay.setEatDailyCalories(0);
            newDay.setRemainingCalories(1600);
            user.getDayRealms().add(newDay);
            user.getDayRealms().get(user.getDayRealms().size()-1).getFoods().add(foodRealm);
            realm.commitTransaction();
        }

        foodView.navigateToSearch();
    }
    @Override
    public UserRealm getCurrentUserRealm() {
        final UserRealm userRealms = realm.where(UserRealm.class)
                .equalTo("email", Session.getInstance().getEmail())
                .findFirst();
        return userRealms;
    }
}
