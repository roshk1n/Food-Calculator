package com.example.roshk1n.foodcalculator.realm;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by roshk1n on 7/23/2016.
 */
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
