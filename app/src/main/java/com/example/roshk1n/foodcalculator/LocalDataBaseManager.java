package com.example.roshk1n.foodcalculator;

import com.example.roshk1n.foodcalculator.realm.DayRealm;
import com.example.roshk1n.foodcalculator.realm.FavoriteListRealm;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

public class LocalDataBaseManager {

    private final Realm realm = Realm.getDefaultInstance();

    public LocalDataBaseManager() {}

    public ArrayList<Food> loadFoodsData(Date date) {
        RealmList<FoodRealm> foodRealms = new RealmList<>();

        for (int i = 0; i < getCurrentUserRealm().getDayRealms().size(); i++) {
            if(compareLongAndDate(getCurrentUserRealm().getDayRealms().get(i).getDate(),date)) {
                foodRealms = getCurrentUserRealm().getDayRealms().get(i).getFoods();
            }
        }

        ArrayList<Food> foods = new ArrayList<>();
        for (int i = 0; i < foodRealms.size(); i++) {
            foods.add(foodRealms.get(i).converToModel());
        }

        return foods;
    }

    public void removeFood(final int indexday, final int indexRemove) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getCurrentUserRealm().getDayRealms().get(indexday).getFoods().get(indexRemove).deleteFromRealm();
            }
        });
    }

    public ArrayList<Food> getFavoriteFood() {
        FavoriteListRealm favoriteListRealm = new FavoriteListRealm();
        if(getCurrentUserRealm().getFavoriteList()!=null) {
            favoriteListRealm = getCurrentUserRealm().getFavoriteList();
        }
        ArrayList<Food> favoriteList = new ArrayList<>(); //convert to base model
        for (int i = 0; i < favoriteListRealm.getFoods().size(); i++) {
            favoriteList.add(favoriteListRealm.getFoods().get(i).converToModel());
        }
        return favoriteList;
    }

    public void removeFavoriteFood(final int position) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getCurrentUserRealm().getFavoriteList().getFoods().get(position).deleteFromRealm();
            }
        });
    }

    public void removeFavoriteFood(final int position, final FoodRealm addFood) {
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

    private boolean compareLongAndDate(Long UserDate, Date date) {

        Date userDayDate = new Date(UserDate);
        return (userDayDate.getDate()== date.getDate()
                && userDayDate.getYear() == date.getYear()
                && userDayDate.getMonth()== date.getMonth());
    }

    public int loadGoalCalories() {
        return getCurrentUserRealm().getGoalCalories();
    }

    public Day loadDayData(Date date) {
        Day day = new Day();

        for (int i = 0; i < getCurrentUserRealm().getDayRealms().size(); i++) {
            if(compareLongAndDate(getCurrentUserRealm().getDayRealms().get(i).getDate(),date)) {
                day = getCurrentUserRealm().getDayRealms().get(i).convertToModel();
            }
        }
        return day;
    }
}
