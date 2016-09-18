package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.manageres.DataManager;
import com.example.roshk1n.foodcalculator.views.InfoFoodView;
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
import java.util.Locale;

public class InfoFoodPresenterImpl implements InfoFoodPresenter {

    private DataManager dataManager = new DataManager();
    private InfoFoodView infoFoodView;

    @Override
    public void setView(InfoFoodView view) {
        this.infoFoodView = view;
    }

    @Override
    public void addToFavorite(Food food) {
        Food cloneFood = null;
        try {
            cloneFood = cloneFood(food);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (cloneFood != null) {
            DecimalFormat format = new DecimalFormat("#0.0", new DecimalFormatSymbols(Locale.US));
            format.setDecimalSeparatorAlwaysShown(false);

            for (int i = 0; i < cloneFood.getNutrients().size(); i++) { // add to Favorite one portion
                float value = Float.valueOf(cloneFood.getNutrients().get(i).getValue()) / cloneFood.getPortion();
                cloneFood.getNutrients().get(i).setValue(String.valueOf(format.format(value)));
            }

            dataManager.addFavoriteFood(cloneFood, new StateItemCallback() {
                @Override
                public void updateImageFavorite(boolean state) {
                    infoFoodView.updateFavoriteImage(state);
                }
            });
        }
    }

    @Override
    public void removeFromFavorite(String ndbno) {
        dataManager.removeFavoriteFoodDB(ndbno, new StateItemCallback() {
            @Override
            public void updateImageFavorite(boolean state) {
                infoFoodView.updateFavoriteImage(state);
            }
        });
    }

    @Override
    public void isExistFavorite(Food food) {
        dataManager.isExistInFavorite(food, new DataAddFoodCallback() {
            @Override
            public void setExistFavorite(boolean existInFavotite) {
                infoFoodView.updateFavoriteImage(existInFavotite);
            }
        });
    }

    @Override
    public void destroy() {
        infoFoodView = null;
    }

    private Food cloneFood(Food food) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(baos);
        ous.writeObject(food);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Food) ois.readObject();
    }

}
