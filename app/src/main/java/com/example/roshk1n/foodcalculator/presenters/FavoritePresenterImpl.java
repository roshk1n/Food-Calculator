package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.Views.FavoriteView;
import com.example.roshk1n.foodcalculator.presenters.FavoritePresenter;
import com.example.roshk1n.foodcalculator.realm.FavoriteListRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

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
        Food deleteFood = getCurrentUserRealm().getFavoriteList().getFoods().get(adapterPosition).converToBaseClass();
        realm.beginTransaction();
        getCurrentUserRealm().getFavoriteList().getFoods().get(adapterPosition).deleteFromRealm();
        realm.commitTransaction();

        favoriteView.makeSnackBar(adapterPosition,deleteFood);
    }

    @Override
    public void addFood(final int position, final Food foodDelete) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getCurrentUserRealm().getFavoriteList().getFoods().add(position,foodDelete.converToRealm());
            }
        });
    }
}
