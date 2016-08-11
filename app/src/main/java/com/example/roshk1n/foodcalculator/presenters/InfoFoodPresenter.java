package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.Views.InfoFoodView;

public interface InfoFoodPresenter {
    void setView(InfoFoodView view);

    void getNutrientsBasic(String ndbno,String type);
}
