package com.example.roshk1n.foodcalculator.presenters;

import android.util.Log;
import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.Views.AddFoodView;
import com.example.roshk1n.foodcalculator.realm.DayRealm;
import com.example.roshk1n.foodcalculator.realm.FavoriteListRealm;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import java.text.DecimalFormat;
import java.util.Date;
import io.realm.Realm;

public class AddFoodPresenterImpl implements AddFoodPresenter {

    private final Realm realm = Realm.getDefaultInstance();

    private AddFoodView foodView;

    @Override
    public void setView(AddFoodView view) {
        foodView = view;
    }

    @Override
    public void addNewFood(Food food) {

        final FoodRealm foodRealm = food.converToRealm();
        final UserRealm user = getCurrentUserRealm();
        for (int i = 0; i < user.getDayRealms().size(); i++) {
            if(compareLongAndDate( getCurrentUserRealm().getDayRealms().get(i).getDate()
                    , new Date(foodRealm.getTime()))) {

                final int finalI1 = i;
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        user.getDayRealms().get(finalI1).getFoods().add(foodRealm);
                    }
                });
                break;

            }  else {
                if(i==user.getDayRealms().size()-1) {
                    final int finalI = i;
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            DayRealm newDay = new DayRealm();
                            newDay.setDate(foodRealm.getTime());
                            newDay.setEatDailyCalories(0);
                            newDay.setRemainingCalories(user.getGoalCalories());
                            user.getDayRealms().add(newDay);
                            user.getDayRealms().get(finalI +1).getFoods().add(foodRealm);
                        }
                    });
                    break;
                }
            }
        }
        if(user.getDayRealms().size()==0) { //if it`s first day for this user
            realm.beginTransaction();
            DayRealm newDay = new DayRealm();
            newDay.setDate(foodRealm.getTime());
            newDay.setEatDailyCalories(0);
            newDay.setRemainingCalories(user.getGoalCalories());
            user.getDayRealms().add(newDay);
            user.getDayRealms().get(user.getDayRealms().size()-1).getFoods().add(foodRealm);
            realm.commitTransaction();
        }

        foodView.navigateToDiary();
    }
    @Override
    public UserRealm getCurrentUserRealm() {
        return realm.where(UserRealm.class)
                .equalTo("email", Session.getInstance().getEmail())
                .findFirst();
    }

    @Override
    public void updateUI(Food food, int numberOfServing) {

        float protein=0,cabs=0,fat=0;
        int calories=0;
        if(isFloat(food.getNutrients().get(0).getGm()))
            protein = Float.valueOf(food.getNutrients().get(0).getGm()) * numberOfServing;

        if (isFloat(food.getNutrients().get(1).getGm()))
            calories = Integer.valueOf(food.getNutrients().get(1).getGm()) * numberOfServing;

        if(isFloat(food.getNutrients().get(2).getGm()))
            fat = Float.valueOf(food.getNutrients().get(2).getGm()) * numberOfServing;

        if(isFloat(food.getNutrients().get(3).getGm()))
            cabs = Float.valueOf(food.getNutrients().get(3).getGm()) * numberOfServing;

        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);

        foodView.setNutrients(String.valueOf(format.format(protein))
                ,String.valueOf(calories)
                ,String.valueOf(format.format(fat))
                ,String.valueOf(format.format(cabs))
                ,food.getName());
    }

    @Override
    public Food updateFood(Food foodForUpdate,String protein, String calories, String fat, String cabs, String name, String number) {

        foodForUpdate.getNutrients().get(0).setGm(protein);
        foodForUpdate.getNutrients().get(1).setGm(calories);
        foodForUpdate.getNutrients().get(2).setGm(fat);
        foodForUpdate.getNutrients().get(3).setGm(cabs);
        foodForUpdate.setName(name);
        foodForUpdate.setPortion(Integer.valueOf(number));
        return foodForUpdate;
    }

    @Override
    public boolean addToFavorite(Food food) {
        FoodRealm foodRealm = food.converToRealm();
        boolean check= false;
        if(getCurrentUserRealm().getFavoriteList() != null) {
            if(!foodRealm.isExistIn(getCurrentUserRealm().getFavoriteList().getFoods())) {

                realm.beginTransaction();
                getCurrentUserRealm().getFavoriteList().getFoods().add(foodRealm);
                realm.commitTransaction();
                Log.d("My",getCurrentUserRealm().getFavoriteList().getFoods().size()+ "s");
                check = true;
            }

        } else {
            realm.beginTransaction();
            FavoriteListRealm favoriteListRealm = realm.createObject(FavoriteListRealm.class);
            getCurrentUserRealm().setFavoriteList(favoriteListRealm);
            getCurrentUserRealm().getFavoriteList().getFoods().add(foodRealm);
            realm.commitTransaction();
            check =true;
        }
        return check;
    }

    @Override
    public void removeFromFavorite(Food food) {
        FoodRealm foodRealm = food.converToRealm();

        if(getCurrentUserRealm().getFavoriteList() == null) {
            realm.beginTransaction();
            FavoriteListRealm favoriteListRealm = realm.createObject(FavoriteListRealm.class);
            getCurrentUserRealm().setFavoriteList(favoriteListRealm);
            realm.commitTransaction();
        }
        for(int i =0; i<getCurrentUserRealm().getFavoriteList().getFoods().size();i++) {
            if(foodRealm.getNdbno().equals(getCurrentUserRealm().getFavoriteList().getFoods().get(i).getNdbno())) {
                realm.beginTransaction();
                getCurrentUserRealm().getFavoriteList().getFoods().remove(i);
                realm.commitTransaction();
            }
        }
    }

    @Override
    public void isExistFavorite(Food food) {
        FoodRealm foodRealm = food.converToRealm();
        if(getCurrentUserRealm().getFavoriteList() != null)
        foodView.updateFavoriteImage(foodRealm.isExistIn(getCurrentUserRealm().getFavoriteList().getFoods()));
    }

   private boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean compareLongAndDate(Long UserDate, Date date) {
        Date userDayDate = new Date(UserDate);
        return (userDayDate.getDate()== date.getDate()
                && userDayDate.getYear() == date.getYear()
                && userDayDate.getMonth()== date.getMonth());
    }
}
