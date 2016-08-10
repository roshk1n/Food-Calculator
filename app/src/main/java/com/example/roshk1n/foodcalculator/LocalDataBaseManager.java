package com.example.roshk1n.foodcalculator;

import com.example.roshk1n.foodcalculator.realm.FavoriteListRealm;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import io.realm.Realm;

public class LocalDataBaseManager {

    private final Realm realm = Realm.getDefaultInstance();

    public LocalDataBaseManager() {}

    public FavoriteListRealm getFavoriteFood() {
        FavoriteListRealm favoriteListRealm = new FavoriteListRealm();
        if(getCurrentUserRealm().getFavoriteList()!=null) {
            favoriteListRealm = getCurrentUserRealm().getFavoriteList();
        }
        return favoriteListRealm;
    }

    public Food removeFavoriteFood(final int position) {
        Food deleteFood = getCurrentUserRealm().getFavoriteList().getFoods().get(position).converToResponseClass();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getCurrentUserRealm().getFavoriteList().getFoods().get(position).deleteFromRealm();
            }
        });
        return deleteFood;
    }

    public void addRemovedFavoriteFood(final int position, final FoodRealm addFood) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getCurrentUserRealm().getFavoriteList().getFoods().add(position,addFood);
            }
        });
    }

    private UserRealm getCurrentUserRealm() {
        return realm.where(UserRealm.class)
                .equalTo("email", Session.getInstance().getEmail())
                .findFirst();
    }
}
