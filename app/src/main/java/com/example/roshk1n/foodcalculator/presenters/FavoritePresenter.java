package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.views.FavoriteView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.util.ArrayList;

public interface FavoritePresenter {

    void setView(FavoriteView view);

    ArrayList<Food> getFavoriteList();

    void removeFavoriteFoodDB(String ndbno);

    void destroy();
}
