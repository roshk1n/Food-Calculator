package com.example.roshk1n.foodcalculator.main.fragments.favorite;

import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.realm.FavoriteListRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import io.realm.Realm;

public class FavoritePresenterImpl implements FavoritePresenter {
    private FavoriteView favoriteView;

    private final Realm realm = Realm.getDefaultInstance();

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
        return realm.where(UserRealm.class)
                .equalTo("email", Session.getInstance().getEmail())
                .findFirst();
    }

    @Override
    public void removeFood(int adapterPosition) {
        realm.beginTransaction();
        getCurrentUserRealm().getFavoriteList().getFoods().get(adapterPosition).deleteFromRealm();
        realm.commitTransaction();
    }
}
