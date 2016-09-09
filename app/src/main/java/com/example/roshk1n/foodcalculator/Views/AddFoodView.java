package com.example.roshk1n.foodcalculator.views;

public interface AddFoodView {
    void navigateToDiary();

    void setNutrients(String calories, String protein, String fat, String cabs, String name);

    void updateFavoriteImage(boolean existIn);
}
