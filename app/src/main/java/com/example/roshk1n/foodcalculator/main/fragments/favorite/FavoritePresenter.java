package com.example.roshk1n.foodcalculator.main.fragments.favorite;

import com.example.roshk1n.foodcalculator.realm.FavoriteListRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

/**
 * Created by roshk1n on 8/2/2016.
 */
public interface FavoritePresenter {

    void setView(FavoriteView view);

    FavoriteListRealm getFavoriteList();

    UserRealm getCurrentUserRealm();

    void removeFood(int adapterPosition);

    void addFood(int position, Food foodRealm);
}
