package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.manageres.DataManager;
import com.example.roshk1n.foodcalculator.interfaces.StateItemCallback;
import com.example.roshk1n.foodcalculator.views.FavoriteView;
import com.example.roshk1n.foodcalculator.interfaces.DataFavoriteCallback;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.util.ArrayList;

public class FavoritePresenterImpl implements FavoritePresenter{

    private DataManager dataManager = new DataManager();
    private ArrayList<Food> favoriteFood = new ArrayList<>();

    private FavoriteView favoriteView;

    @Override
    public void setView(FavoriteView favoriteView) {
        this.favoriteView = favoriteView;
    }

    @Override
    public ArrayList<Food> getFavoriteList() {
        dataManager.loadFavoriteList(new DataFavoriteCallback() {
            @Override
            public void setFavoriteList(ArrayList<Food> favFoods) {
                if(favoriteView != null)
                    favoriteView.setFavoriteList(favFoods);
            }
        });
        return favoriteFood;
    }

    @Override
    public void removeFavoriteFoodDB(String ndbno) {
        dataManager.removeFavoriteFoodDB(ndbno, new StateItemCallback() {
            @Override
            public void updateImageFavorite(boolean state) {
                //remove success
            }
        });
    }

    @Override
    public void destroy() {
        favoriteView = null;
    }
}
