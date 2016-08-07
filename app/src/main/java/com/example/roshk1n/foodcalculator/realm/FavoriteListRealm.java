package com.example.roshk1n.foodcalculator.realm;

import io.realm.RealmList;
import io.realm.RealmObject;

public class FavoriteListRealm extends RealmObject{

    private RealmList<FoodRealm> foods = new RealmList<FoodRealm>();

    public FavoriteListRealm() {
    }

    public RealmList<FoodRealm> getFoods() {
        return foods;
    }

    public void setFoods(RealmList<FoodRealm> foods) {
        this.foods = foods;
    }
}
