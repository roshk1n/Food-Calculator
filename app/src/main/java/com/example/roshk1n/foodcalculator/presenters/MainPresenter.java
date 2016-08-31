package com.example.roshk1n.foodcalculator.presenters;

import android.graphics.Bitmap;

import com.example.roshk1n.foodcalculator.views.MainView;

/**
 * Created by roshk1n on 8/16/2016.
 */
public interface MainPresenter {
    void setView (MainView view);

    Bitmap stringToBitmap(String photoUrl);

    void updateInfoUser(Bitmap resource);

    Bitmap getLocalImage();
}
