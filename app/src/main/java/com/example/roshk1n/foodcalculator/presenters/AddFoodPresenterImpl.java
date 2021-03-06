package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.manageres.DataManager;
import com.example.roshk1n.foodcalculator.manageres.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.views.AddFoodView;
import com.example.roshk1n.foodcalculator.interfaces.DataAddFoodCallback;
import com.example.roshk1n.foodcalculator.interfaces.StateItemCallback;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class AddFoodPresenterImpl implements AddFoodPresenter {
    private DataManager dataManager = new DataManager();

    private AddFoodView foodView;

    @Override
    public void setView(AddFoodView view) {
        foodView = view;
    }

    @Override
    public void addNewFood(Food food) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(food.getTime());
        int indexDay = LocalDataBaseManager.dayIsExist(calendar);
        if (indexDay != -1) {
            LocalDataBaseManager.addFood(food);

        } else {
            LocalDataBaseManager.createDay(food.getTime());
            LocalDataBaseManager.addFood(food);
        }
        dataManager.addFood(food);
        if (foodView != null)
            foodView.navigateToDiary();
    }

    @Override
    public void updateUI(Food food, int numberOfServing) {
        Food cloneFood = null;
        try {
            cloneFood = cloneFood(food);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (cloneFood != null) {
            DecimalFormat format = new DecimalFormat("#0.0", new DecimalFormatSymbols(Locale.US));
            format.setDecimalSeparatorAlwaysShown(false);

            for (int i = 0; i < food.getNutrients().size(); i++) {
                if (isFloat(food.getNutrients().get(i).getValue())) {
                    float value = Float.valueOf(food.getNutrients().get(i).getValue()) * numberOfServing;
                    cloneFood.getNutrients().get(i).setValue(String.valueOf(format.format(value)));
                }
            }

            if (foodView != null)
                foodView.setNutrients(cloneFood.getNutrients().get(1).getValue()
                        , cloneFood.getNutrients().get(2).getValue()
                        , cloneFood.getNutrients().get(3).getValue()
                        , cloneFood.getNutrients().get(4).getValue()
                        , cloneFood.getName());
        }
    }

    @Override
    public Food updateFood(Food foodForUpdate, String calories, String protein, String fat, String cabs, String name, String number) {

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
        dataManager.addFavoriteFood(food, new StateItemCallback() {
            @Override
            public void updateImageFavorite(boolean state) {
                if (foodView != null)
                    foodView.updateFavoriteImage(state);
            }
        });
    }

    @Override
    public void removeFromFavorite(String ndbno) {
        dataManager.removeFavoriteFoodDB(ndbno, new StateItemCallback() {
            @Override
            public void updateImageFavorite(boolean state) {
                if (foodView != null)
                    foodView.updateFavoriteImage(state);
            }
        });
    }

    @Override
    public void isExistFavorite(final Food food) {
        dataManager.isExistInFavorite(food, new DataAddFoodCallback() {
            @Override
            public void setExistFavorite(boolean existInFavorite) {
                if (foodView != null)
                    foodView.updateFavoriteImage(existInFavorite);
            }
        });
    }

    @Override
    public void destroy() {
        foodView = null;
    }

    private Food cloneFood(Food food) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(baos);
        ous.writeObject(food);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Food) ois.readObject();
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
