package com.example.roshk1n.foodcalculator.main.fragments.infoFood;

import android.util.Log;

import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.realm.DayRealm;
import com.example.roshk1n.foodcalculator.realm.FavoriteListRealm;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.jar.Pack200;

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
            if(compareLongAndDate( getCurrentUserRealm().getDayRealms().get(i).getDate()
                    , new Date(foodRealm.getTime()))) {

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
    public Food updateFood(Food foodForUpdate,String protein, String calories, String fat, String cabs, String name, String number) {

        foodForUpdate.getNutrients().get(0).setValue(protein);
        foodForUpdate.getNutrients().get(1).setValue(calories);
        foodForUpdate.getNutrients().get(2).setValue(fat);
        foodForUpdate.getNutrients().get(3).setValue(cabs);
        foodForUpdate.setName(name);
        foodForUpdate.setPortion(Integer.valueOf(number));
        return foodForUpdate;
    }

    @Override
    public boolean addToFavorite(Food food) {
        FoodRealm foodRealm = food.converToRealm();
        boolean check= false;
        if(getCurrentUserRealm().getFavoriteList() != null) {
            if(!foodRealm.isExistIn(getCurrentUserRealm().getFavoriteList().getFoods())) {

                realm.beginTransaction();
                getCurrentUserRealm().getFavoriteList().getFoods().add(foodRealm);
                realm.commitTransaction();
                Log.d("My",getCurrentUserRealm().getFavoriteList().getFoods().size()+ "s");
                check = true;
            }
        }
        return check;
    }

    @Override
    public void removeFromFavorite(Food food) {
        FoodRealm foodRealm = food.converToRealm();

        if(getCurrentUserRealm().getFavoriteList() == null) {
            realm.beginTransaction();
            FavoriteListRealm favoriteListRealm = realm.createObject(FavoriteListRealm.class);
            getCurrentUserRealm().setFavoriteList(favoriteListRealm);
            realm.commitTransaction();
        }
        for(int i =0; i<getCurrentUserRealm().getFavoriteList().getFoods().size();i++) {
            if(foodRealm.getNdbno().equals(getCurrentUserRealm().getFavoriteList().getFoods().get(i).getNdbno())) {
                realm.beginTransaction();
                getCurrentUserRealm().getFavoriteList().getFoods().remove(i);
                realm.commitTransaction();
            }
        }

    }

    @Override
    public void isExistFavorite(Food food) {

        FoodRealm foodRealm = food.converToRealm();
        foodView.updateFavoriteImage(foodRealm.isExistIn(getCurrentUserRealm().getFavoriteList().getFoods()));

    }

    boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean compareLongAndDate(Long UserDate, Date date) {

        Date userDayDate = new Date(UserDate);
        if(userDayDate.getDate()== date.getDate()
                && userDayDate.getYear() == date.getYear()
                && userDayDate.getMonth()== date.getMonth()) {
            return true;

        } else {
            return false;
        }
    }
}
