package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.Views.InfoFoodView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

public class InfoFoodPresenterImpl implements InfoFoodPresenter {

    private LocalDataBaseManager localDataBaseManager = new LocalDataBaseManager();
    private InfoFoodView infoFoodView;

    @Override
    public void setView(InfoFoodView view) {
        this.infoFoodView = view;
    }

    @Override
    public void addToFavorite(Food food) {
        localDataBaseManager.addFavoriteFood(food);
        infoFoodView.updateFavoriteImage(true);
    }

    @Override
    public void removeFromFavorite(String ndbno) {
        localDataBaseManager.removeFavoriteFoodDB(ndbno);
        infoFoodView.updateFavoriteImage(false);
    }

    @Override
    public void isExistFavorite(Food food) {
        infoFoodView.updateFavoriteImage(localDataBaseManager.isExistInFavotite(food));
    }
}
