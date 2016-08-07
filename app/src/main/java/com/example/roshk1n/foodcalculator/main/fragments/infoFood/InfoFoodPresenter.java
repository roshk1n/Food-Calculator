package com.example.roshk1n.foodcalculator.main.fragments.infoFood;

import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

/**
 * Created by roshk1n on 7/25/2016.
 */

public interface InfoFoodPresenter {

    void setView(InfoFoodView view);

    void addNewFood(Food food);

     UserRealm getCurrentUserRealm();

    void updateUI(Food food, int numberOfServing);

    Food updateFood(Food foodForUpdate,String protein, String calories, String fat, String cabs, String name, String number);

    boolean addToFavorite(Food food);

    void removeFromFavorite(Food food);

    void isExistFavorite(Food food);
}
