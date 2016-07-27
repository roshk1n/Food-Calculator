package com.example.roshk1n.foodcalculator.main.fragments.infoFood;

import android.util.Log;

import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.realm.DayRealm;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.text.DecimalFormat;

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

        foodView.navigateToDiary();
    }
    @Override
    public UserRealm getCurrentUserRealm() {
        final UserRealm userRealms = realm.where(UserRealm.class)
                .equalTo("email", Session.getInstance().getEmail())
                .findFirst();
        return userRealms;
    }


    @Override
    public void updateUI(Food food, int numberOfServing) {

        float protein=0,cabs=0,fat=0;
        int calories=0;
        if(isFloat(food.getNutrients().get(0).getValue()))
            protein = Float.valueOf(food.getNutrients().get(0).getValue()) * numberOfServing;

        if (isFloat(food.getNutrients().get(1).getValue()))
            calories = Integer.valueOf(food.getNutrients().get(1).getValue()) * numberOfServing;

        if(isFloat(food.getNutrients().get(2).getValue()))
            fat = Float.valueOf(food.getNutrients().get(2).getValue()) * numberOfServing;

        if(isFloat(food.getNutrients().get(3).getValue()))
            cabs = Float.valueOf(food.getNutrients().get(3).getValue()) * numberOfServing;

        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);

        foodView.setNutrients(String.valueOf(format.format(protein))
                ,String.valueOf(calories)
                ,String.valueOf(format.format(fat))
                ,String.valueOf(format.format(cabs))
                ,food.getName().toString());
    }

    @Override
    public Food updateFood(Food foodForUpdate,String protein, String calories, String fat, String cabs, String name) {

        foodForUpdate.getNutrients().get(0).setValue(protein);
        foodForUpdate.getNutrients().get(1).setValue(calories);
        foodForUpdate.getNutrients().get(2).setValue(fat);
        foodForUpdate.getNutrients().get(3).setValue(cabs);
        foodForUpdate.setName(name);
        return foodForUpdate;
    }

    boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
