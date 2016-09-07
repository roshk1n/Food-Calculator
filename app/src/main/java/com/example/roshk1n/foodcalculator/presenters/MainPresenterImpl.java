package com.example.roshk1n.foodcalculator.presenters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.roshk1n.foodcalculator.manageres.DataManager;
import com.example.roshk1n.foodcalculator.manageres.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.views.MainView;

import java.io.ByteArrayOutputStream;

public class MainPresenterImpl implements MainPresenter {
    private MainView mainView;
    private DataManager dataManager = new DataManager();

    @Override
    public void setView(MainView view) {
        this.mainView = view;
    }

    @Override
    public Bitmap stringToBitmap(String photoUrl) {
        Bitmap imageUser = null;
        try {
            byte [] encodeByte=Base64.decode(photoUrl, Base64.DEFAULT);
            imageUser = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
        }
        return imageUser;
    }

    @Override
    public void updateInfoUser(Bitmap resource) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resource.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String image = Base64.encodeToString(b, Base64.DEFAULT);
        dataManager.updateInfoUser(image);
    }

    @Override
    public Bitmap getLocalImage() {
        String image = LocalDataBaseManager.getLocalUserImage();
        return stringToBitmap(image);
    }
}
