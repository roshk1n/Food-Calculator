package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.views.AddFoodView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.io.IOException;

public interface AddFoodPresenter {

    void setView(AddFoodView view);

    void addNewFood(Food food);

    void updateUI(Food food, int numberOfServing) throws IOException, ClassNotFoundException;

    Food updateFood(Food foodForUpdate,String protein, String calories, String fat, String cabs, String name, String number);

    void addToFavorite(Food food);

    void removeFromFavorite(String ndbno);

    void isExistFavorite(Food food);
}
