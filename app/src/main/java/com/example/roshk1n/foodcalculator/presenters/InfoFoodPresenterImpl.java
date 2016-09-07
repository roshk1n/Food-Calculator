package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.manageres.DataManager;
import com.example.roshk1n.foodcalculator.views.InfoFoodView;
import com.example.roshk1n.foodcalculator.interfaces.DataAddFoodCallback;
import com.example.roshk1n.foodcalculator.interfaces.StateItemCallback;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

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
        DecimalFormat format = new DecimalFormat("#0.0",new DecimalFormatSymbols(Locale.US));
        format.setDecimalSeparatorAlwaysShown(false);

        for (int i = 0; i < food.getNutrients().size(); i++) { // add to Favorite one portion
            float value = Float.valueOf(food.getNutrients().get(i).getValue())/food.getPortion();
            food.getNutrients().get(i).setValue(String.valueOf(format.format(value)));
        }

        dataManager.addFavoriteFood(food, new StateItemCallback() {
            @Override
            public void updateImageFavorite(boolean state) {
                infoFoodView.updateFavoriteImage(state);
            }
        });
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

        //  infoFoodView.updateFavoriteImage(localDataBaseManager.isExistInFavotite(food));
    }
}
