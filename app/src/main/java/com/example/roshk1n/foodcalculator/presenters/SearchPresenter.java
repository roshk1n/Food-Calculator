package com.example.roshk1n.foodcalculator.presenters;

import android.content.Context;

public interface SearchPresenter {
    void searchFood(Context context, String search);

    void destroy();
}
