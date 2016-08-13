package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.Views.AddFoodView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Nutrient;

import java.text.DecimalFormat;
import java.util.Date;

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
            localDataBaseManager.addFood(food);

        } else {
            localDataBaseManager.createDay(food.getTime());
            localDataBaseManager.addFood(food);
        }

        foodView.navigateToDiary();
    }

    @Override
    public void updateUI(Food food, int numberOfServing) {

        float protein=0,cabs=0,fat=0,calories=0;

        if (isFloat(food.getNutrients().get(1).getValue()))
            calories = Integer.valueOf(food.getNutrients().get(1).getValue()) * numberOfServing;

        if(isFloat(food.getNutrients().get(2).getValue()))
            protein = Float.valueOf(food.getNutrients().get(2).getValue()) * numberOfServing;

        if(isFloat(food.getNutrients().get(3).getValue()))
            fat = Float.valueOf(food.getNutrients().get(3).getValue()) * numberOfServing;

        if(isFloat(food.getNutrients().get(4).getValue()))
            cabs = Float.valueOf(food.getNutrients().get(4).getValue()) * numberOfServing;

        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);

        foodView.setNutrients(String.valueOf(format.format(calories))
                ,String.valueOf(format.format(protein))
                ,String.valueOf(format.format(fat))
                ,String.valueOf(format.format(cabs))
                ,food.getName());
    }

    @Override
    public Food updateFood(Food foodForUpdate,String calories, String protein, String fat, String cabs, String name, String number) {

        foodForUpdate.getNutrients().get(1).setValue(calories);
        foodForUpdate.getNutrients().get(2).setValue(protein);
        foodForUpdate.getNutrients().get(3).setValue(fat);
        foodForUpdate.getNutrients().get(4).setValue(cabs);
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
