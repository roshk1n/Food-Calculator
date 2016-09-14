package com.example.roshk1n.foodcalculator.views;

import android.content.Context;

public interface RemindersView {
    void setTime(int position, long time);

    Context getContext();
}
