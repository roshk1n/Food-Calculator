package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.DataManaget;
import com.example.roshk1n.foodcalculator.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.Views.AddFoodView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Nutrient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.Date;

public class AddFoodPresenterImpl implements AddFoodPresenter {

    private LocalDataBaseManager localDataBaseManager = new LocalDataBaseManager();
    private DataManaget dataManaget = new DataManaget();

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
        dataManaget.addFood(food);
        foodView.navigateToDiary();

    }

    @Override
    public void updateUI(Food food, int numberOfServing) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream  ous = new ObjectOutputStream(baos);
        ous.writeObject(food);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Food clonFood = (Food)ois.readObject();

        DecimalFormat format = new DecimalFormat("#0.0");
        format.setDecimalSeparatorAlwaysShown(false);

        for (int i = 0; i< food.getNutrients().size(); i++) {
            if (isFloat(food.getNutrients().get(i).getValue())) {
                float value = Float.valueOf(food.getNutrients().get(i).getValue()) * numberOfServing;
                clonFood.getNutrients().get(i).setValue(String.valueOf(format.format(value)));
            }
        }

        foodView.setNutrients(clonFood.getNutrients().get(1).getValue()
                ,clonFood.getNutrients().get(2).getValue()
                ,clonFood.getNutrients().get(3).getValue()
                ,clonFood.getNutrients().get(4).getValue()
                ,clonFood.getName());
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
