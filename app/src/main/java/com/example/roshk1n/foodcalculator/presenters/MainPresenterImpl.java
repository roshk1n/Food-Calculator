package com.example.roshk1n.foodcalculator.presenters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.Views.MainView;

/**
 * Created by roshk1n on 8/16/2016.
 */
public class MainPresenterImpl implements MainPresenter {
    private MainView mainView;

    @Override
    public void setView(MainView view) {
        this.mainView = view;
    }

    @Override
    public Bitmap stringToBitmap(String photoUrl) {
        Bitmap image = null;
        try {
            byte [] encodeByte= Base64.decode(Session.getInstance().getUrlPhoto(), Base64.DEFAULT);
            image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
        }
        return image;
    }
}
