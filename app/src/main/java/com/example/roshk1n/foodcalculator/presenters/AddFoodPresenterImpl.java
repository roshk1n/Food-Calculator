package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.Views.AddFoodView;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Nutrient;

import java.text.DecimalFormat;
import java.util.Date;
import io.realm.Realm;

public class AddFoodPresenterImpl implements AddFoodPresenter {

    private LocalDataBaseManager localDataBaseManager = new LocalDataBaseManager();

    private AddFoodView foodView;

    @Override
    public void setView(AddFoodView view) {
        foodView = view;
    }

    @Override
    public void addNewFood(Food food) {
        int indexDay = localDataBaseManager.dayIsExist(new Date(food.getTime()));
        localDataBaseManager.loadDayData(new Date(food.getTime()));
        if(indexDay!=-1) {
            localDataBaseManager.addFood(food,indexDay);

        } else {
            localDataBaseManager.createDay(food.getTime());
            localDataBaseManager.addFood(food,indexDay);
        }

        foodView.navigateToDiary();
    }

    @Override
    public void updateUI(Food food, int numberOfServing) {

        float protein,cabs,fat,calories;

        for (Nutrient nutrient : food.getNutrients()) {
            nutrient.setGm(String.valueOf(Float.parseFloat(nutrient.getGm()) * numberOfServing));
        }

        protein = Float.parseFloat(food.getNutrients().get(0).getGm());
        calories = Float.parseFloat(food.getNutrients().get(1).getGm());
        fat = Float.parseFloat(food.getNutrients().get(2).getGm());
        cabs = Float.parseFloat(food.getNutrients().get(3).getGm());

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
