package com.example.roshk1n.foodcalculator.interfaces;

import android.support.v7.widget.Toolbar;

public interface OnFragmentListener {
    void setDrawerMenu();

    void setArrowToolbar();

    void enableMenuSwipe();

    void disabledMenuSwipe();

    void updateDrawer();

    void updateDrawerLight();

    void hideToolbar();

    void showToolbar();

    void setTitle(String title);

    void setSupportActionBar(Toolbar toolbar);
}
