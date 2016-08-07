package com.example.roshk1n.foodcalculator.main.fragments.favorite;

import com.example.roshk1n.foodcalculator.realm.FavoriteListRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;

/**
 * Created by roshk1n on 8/2/2016.
 */
public interface FavoritePresenter {

    void setView(FavoriteView view);

    FavoriteListRealm getFavoriteList();

    UserRealm getCurrentUserRealm();

    void removeFood(int adapterPosition);
}
