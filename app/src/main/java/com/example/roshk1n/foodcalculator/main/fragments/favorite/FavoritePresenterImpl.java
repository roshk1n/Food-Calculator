package com.example.roshk1n.foodcalculator.main.fragments.favorite;

import android.view.View;

import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.realm.FavoriteListRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;

import io.realm.Realm;

/**
 * Created by roshk1n on 8/2/2016.
 */
public class FavoritePresenterImpl implements FavoritePresenter {
    private FavoriteView favoriteView;

    private Realm realm = Realm.getDefaultInstance();

    @Override
    public void setView(FavoriteView favoriteView) {
        this.favoriteView = favoriteView;
    }

    @Override
    public FavoriteListRealm getFavoriteList() {
        FavoriteListRealm favoriteListRealm = new FavoriteListRealm();
        if(getCurrentUserRealm().getFavoriteList()!=null) {
            favoriteListRealm = getCurrentUserRealm().getFavoriteList();
        }
        return favoriteListRealm;
    }

    @Override
    public UserRealm getCurrentUserRealm() {
        final UserRealm userRealms = realm.where(UserRealm.class)
                .equalTo("email", Session.getInstance().getEmail())
                .findFirst();
        return userRealms;
    }

    @Override
    public void removeFood(int adapterPosition) {
        realm.beginTransaction();
        getCurrentUserRealm().getFavoriteList().getFoods().get(adapterPosition).deleteFromRealm();
        realm.commitTransaction();
    }
}
