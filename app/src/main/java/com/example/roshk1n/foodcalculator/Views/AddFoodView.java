package com.example.roshk1n.foodcalculator.Views;

/**
 * Created by roshk1n on 7/25/2016.
 */

public interface AddFoodView {

    void navigateToDiary();

    void setNutrients(String protein, String calories, String fat, String cabs, String name);

    void updateFavoriteImage(boolean existIn);
}
