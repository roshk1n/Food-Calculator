package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.Views.FavoriteView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.util.ArrayList;

public class FavoritePresenterImpl implements FavoritePresenter {

    LocalDataBaseManager localDataBaseManager = new LocalDataBaseManager();
    ArrayList<Food> favoriteFood = new ArrayList<>();

    private FavoriteView favoriteView;

    @Override
    public void setView(FavoriteView favoriteView) {
        this.favoriteView = favoriteView;
    }

    @Override
    public ArrayList<Food> getFavoriteList() {
        favoriteFood = localDataBaseManager.getFavoriteFood();
        return favoriteFood;
    }

    @Override
    public void removeFavoriteFoodDB(int position) {
        localDataBaseManager.removeFavoriteFood(position);
    }
}
