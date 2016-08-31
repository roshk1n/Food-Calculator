package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.views.InfoFoodView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

public interface InfoFoodPresenter {
    void setView(InfoFoodView view);

    void addToFavorite(Food food);

    void removeFromFavorite(String ndbno);

    void isExistFavorite(Food food);
}
