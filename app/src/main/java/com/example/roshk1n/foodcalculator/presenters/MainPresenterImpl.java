package com.example.roshk1n.foodcalculator.presenters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.roshk1n.foodcalculator.DataManager;
import com.example.roshk1n.foodcalculator.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.Views.MainView;

import java.io.ByteArrayOutputStream;

/**
 * Created by roshk1n on 8/16/2016.
 */
public class MainPresenterImpl implements MainPresenter {
    private MainView mainView;
    private DataManager dataManager = new DataManager();

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

    @Override
    public void addLocalImage(Bitmap resource) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resource.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String image = Base64.encodeToString(b, Base64.DEFAULT);
        LocalDataBaseManager.addLocalUserImage(image);
    }
}
