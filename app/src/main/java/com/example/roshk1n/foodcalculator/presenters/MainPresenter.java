package com.example.roshk1n.foodcalculator.presenters;

import android.graphics.Bitmap;

import com.example.roshk1n.foodcalculator.views.MainView;

public interface MainPresenter {
    void setView (MainView view);

    Bitmap stringToBitmap(String photoUrl);

    void updateInfoUser(Bitmap resource);

    Bitmap getLocalImage();

    void destroy();
}
