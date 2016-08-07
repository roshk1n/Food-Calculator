package com.example.roshk1n.foodcalculator.main.fragments.diary;

import java.util.Date;

/**
 * Created by roshk1n on 7/19/2016.
 */
public interface DiaryView {
    void setData(Date date);

    void updateCalories(int goal,int eat,int remaining, int checkLimit, int color);
}
