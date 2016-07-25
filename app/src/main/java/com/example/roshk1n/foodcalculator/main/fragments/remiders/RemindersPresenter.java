package com.example.roshk1n.foodcalculator.main.fragments.remiders;

import android.support.v4.app.Fragment;

/**
 * Created by roshk1n on 7/24/2016.
 */
public interface RemindersPresenter {
    void setView(RemindersView view);
    void showTimePicker(String tag, Fragment fragment);

}
