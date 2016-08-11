package com.example.roshk1n.foodcalculator.presenters;

import android.graphics.Color;

import com.example.roshk1n.foodcalculator.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.Views.DiaryView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryPresenterImpl implements DiaryPresenter {
    private LocalDataBaseManager localDataBaseManager = new LocalDataBaseManager();
    private Day day;
    private Date date = new Date();
    private DiaryView diaryView;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public void setView(DiaryView view) {
        diaryView = view;
    }

    @Override
    public Day loadDay() {
        day = localDataBaseManager.loadDayData(date);
        if(day.getFoods().size()==0) {
            diaryView.showHintAddAnim();
        } else {
            diaryView.hideHintAddAnim();
        }
        return day;
    }

    @Override
    public void setFollowDate() {
        date.setDate(date.getDate()-1);
    }

    @Override
    public void setNextDate() {
        date.setDate(date.getDate()+1);
    }

    @Override
    public void removeFoodDB(final int indexRemove) {

        localDataBaseManager.removeFood(indexRemove);
    }

    @Override
    public String getDateString() {
        SimpleDateFormat format1 = new SimpleDateFormat();
        format1.applyPattern("EEEE, dd MMMM");
        return format1.format(date);
    }

    @Override
    public void calculateCalories() {
        if(day.getFoods().size() != 0) {
            int eat_calories = 0;
            for (int j = 0; j < day.getFoods().size(); j++) {
                eat_calories += Math.round(Float.valueOf(day.getFoods().get(j).getNutrients().get(1).getGm()));
            }
            int goalCalories = localDataBaseManager.loadGoalCalories();
            day.setRemainingCalories(goalCalories - eat_calories);

            localDataBaseManager.updateCalories(eat_calories, day.getRemainingCalories());

            String remainingCalories = String.valueOf(day.getRemainingCalories());
            String eatCalories = String.valueOf(eat_calories);
            int checkLimit = checkLimit(day.getRemainingCalories());
            int color = getColor(checkLimit);

            if (checkLimit != 1) { //if need dialog for limit
                diaryView.showDialog(remainingCalories, checkLimit);
            }
            diaryView.updateCalories(eatCalories, remainingCalories, color);
        }
    }

    @Override
    public void getGoalCalories() {
        int goalCalories = localDataBaseManager.loadGoalCalories();
        diaryView.setGoalCalories(String.valueOf(goalCalories));
    }

    private int checkLimit(int remaining) {

        int checkLimit = 1;// if limit not reached

        if(remaining<=300) checkLimit = 2; // if almost reach limit

        if(remaining<=0) checkLimit = 3; // if limit reached

        if(remaining<=-500) checkLimit = 4; // if limit quite a a lot

        return checkLimit;
    }

    private int getColor(int checkLimit) {
        int color = Color.parseColor("#212121");

        if(checkLimit==2) color = Color.parseColor("#7cab26");

        if(checkLimit==3) color = Color.parseColor("#d30b0b");

        if(checkLimit==4) color = Color.parseColor("#a40f0f");

        return color;
    }
}
