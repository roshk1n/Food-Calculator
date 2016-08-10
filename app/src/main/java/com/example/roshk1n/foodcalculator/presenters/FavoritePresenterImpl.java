package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.Views.FavoriteView;
import com.example.roshk1n.foodcalculator.realm.FavoriteListRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

public class FavoritePresenterImpl implements FavoritePresenter {

    LocalDataBaseManager localDataBaseManager = new LocalDataBaseManager();
    private FavoriteView favoriteView;

    @Override
    public void setView(FavoriteView favoriteView) {
        this.favoriteView = favoriteView;
    }

    @Override
    public FavoriteListRealm getFavoriteList() {
        return localDataBaseManager.getFavoriteFood();
    }

    @Override
    public void removeFood(int position) {
        Food deleteFood = localDataBaseManager.removeFavoriteFood(position);

        favoriteView.makeSnackBarAction(position,deleteFood);
    }

    @Override
    public void addRemovedFavoriteFood(int position, Food foodDelete) {
        localDataBaseManager.addRemovedFavoriteFood(position,foodDelete.converToRealm());

        favoriteView.makeSnackBar("Item has been restored.");
    }
}
