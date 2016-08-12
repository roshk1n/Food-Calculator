package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.Views.AddFoodView;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import java.text.DecimalFormat;
import java.util.Date;
import io.realm.Realm;

public class AddFoodPresenterImpl implements AddFoodPresenter {

    private LocalDataBaseManager localDataBaseManager = new LocalDataBaseManager();
    private final Realm realm = Realm.getDefaultInstance();

    private AddFoodView foodView;

    @Override
    public void setView(AddFoodView view) {
        foodView = view;
    }

    @Override
    public void addNewFood(Food food) {
        int indexDay = localDataBaseManager.dayIsExist(new Date(food.getDate()));
        localDataBaseManager.loadDayData(new Date(food.getDate()));
        if(indexDay!=-1) {
            localDataBaseManager.addFood(food,indexDay);

        } else {
            localDataBaseManager.createDay(food.getDate());
            localDataBaseManager.addFood(food,indexDay);
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

        float protein=0,cabs=0,fat=0,calories=0;
        if(isFloat(food.getNutrients().get(0).getGm()))
            protein = Float.valueOf(food.getNutrients().get(0).getGm()) * numberOfServing;

        if (isFloat(food.getNutrients().get(1).getGm()))
            calories = Float.valueOf(food.getNutrients().get(1).getGm()) * numberOfServing;

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
    public void addToFavorite(Food food) {
        localDataBaseManager.addFavoriteFood(food);
        foodView.updateFavoriteImage(true);
    }

    @Override
    public void removeFromFavorite(String ndbno) {
        localDataBaseManager.removeFavoriteFoodDB(ndbno);
        foodView.updateFavoriteImage(false);
    }

    @Override
    public void isExistFavorite(Food food) {
        foodView.updateFavoriteImage(localDataBaseManager.isExistInFavotite(food));
    }

   private boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
