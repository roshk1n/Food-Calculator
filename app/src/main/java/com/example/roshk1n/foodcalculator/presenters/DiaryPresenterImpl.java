package com.example.roshk1n.foodcalculator.presenters;

import android.graphics.Color;

import com.example.roshk1n.foodcalculator.interfaces.LoadDayCallback;
import com.example.roshk1n.foodcalculator.manageres.DataManager;
import com.example.roshk1n.foodcalculator.manageres.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Nutrient;
import com.example.roshk1n.foodcalculator.views.DiaryView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DiaryPresenterImpl implements DiaryPresenter {
    private DataManager dataManager = new DataManager();
    private Day day;
    private Calendar date = Calendar.getInstance();
    private DiaryView diaryView;
    private boolean checkForAnim = true;

    public Calendar getDate() {
        Calendar dateNow = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, dateNow.get(Calendar.HOUR_OF_DAY));
        date.set(Calendar.MINUTE, dateNow.get(Calendar.MINUTE));
        date.set(Calendar.SECOND, dateNow.get(Calendar.SECOND));
        return date;
    }

    public void setDate(Long date) {
        this.date.setTimeInMillis(date);
    }

    @Override
    public void setView(DiaryView view) {
        diaryView = view;
    }

    @Override
    public void loadDay() {
        checkForAnim = true;
        dataManager.loadDayData(date, new LoadDayCallback() {
            @Override
            public void loadComplete(Day d) {
                if (diaryView != null) {
                    day = d;
                    diaryView.setDay(d);
                    if (day.getFoods().size() == 0) {
                        if (!checkForAnim && diaryView != null) {
                            diaryView.showHintAddAnim();
                        }
                    }
                    checkForAnim = false;
                }
            }
        });
        dataManager.fillFirebaseFood(generateFood());
    }

    private ArrayList<Food> generateFood() {
        ArrayList<Food> foods = new ArrayList<>();
        ArrayList<Nutrient> nutrients = new ArrayList<>();

        Nutrient nutrientEnergy = new Nutrient("208", "Калорій", "Energy", "kcal", "100");
        Nutrient nutrientFat = new Nutrient("204", "Жири", "Fat", "g", "9.1");
        Nutrient nutrientCholesterol = new Nutrient("2", "Холестерин", "Cholesterol", "mg", "9.1");
        Nutrient nutrientSodium = new Nutrient("3", "Натрій", "Sodium", "mg", "9.1");
        Nutrient nutrientPotassium = new Nutrient("4", "Калій", "Potassium", "mg", "9.1");
        Nutrient nutrientCarbohydrate = new Nutrient("291", "Вуглеводи", "Carbohydrate, by difference", "g", "9.1");
        Nutrient nutrientSugars = new Nutrient("269", "Цукор", "Sugars, total", "g", "9.1");
        Nutrient nutrientProtein = new Nutrient("203", "Білки", "Protein", "g", "9.1");
        Nutrient nutrientVitaminA = new Nutrient("5", "Вітамін A", "Vitamin A", "%", "9.1");
        Nutrient nutrientVitaminC = new Nutrient("6", "Вітамін С", "Vitamin C", "%", "9.1");
        Nutrient nutrientCalcium = new Nutrient("301", "Кальцій С", "Calcium, Ca", "%", "9.1");
        Nutrient nutrientIron = new Nutrient("1", "Залізо" ,"Iron, Fe", "%", "9.1");

        nutrients.add(nutrientEnergy);
        nutrients.add(nutrientProtein);
        nutrients.add(nutrientFat);
        nutrients.add(nutrientCarbohydrate);
        nutrients.add(nutrientSugars);
        nutrients.add(nutrientCalcium);
        nutrients.add(nutrientIron);
        nutrients.add(nutrientCholesterol);
        nutrients.add(nutrientSodium);
        nutrients.add(nutrientPotassium);
        nutrients.add(nutrientVitaminA);
        nutrients.add(nutrientVitaminC);

        Food food = new Food();
        food.setName("молоко");
        food.setNameEng("milk");
        food.setNdbno("1");
        food.setNutrients(nutrients);
        foods.add(food);

        return foods;
    }

    @Override
    public void setPreviousDate() {
        Calendar dateNow = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, dateNow.get(Calendar.HOUR_OF_DAY));
        date.set(Calendar.MINUTE, dateNow.get(Calendar.MINUTE));
        date.set(Calendar.SECOND, dateNow.get(Calendar.SECOND));
        date.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH) - 1);
    }

    @Override
    public void setNextDate() {
        Calendar dateNow = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, dateNow.get(Calendar.HOUR_OF_DAY));
        date.set(Calendar.MINUTE, dateNow.get(Calendar.MINUTE));
        date.set(Calendar.SECOND, dateNow.get(Calendar.SECOND));

        date.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH) + 1);
    }

    @Override
    public void destroy() {
        diaryView = null;
    }

    @Override
    public void removeFoodDB(int index, long time) {
        dataManager.removeFood(index, time);
    }

    @Override
    public String getDateString() {
        SimpleDateFormat format1 = new SimpleDateFormat();
        format1.applyPattern("EEEE, dd MMMM");
        return format1.format(date.getTime());
    }

    @Override
    public void calculateCalories() {
        int eat_calories = 0;
        for (int j = 0; j < day.getFoods().size(); j++) {
            eat_calories += Math.round(Float.valueOf(day.getFoods().get(j).getNutrients().get(1).getValue()));
        }
        int goalCalories = dataManager.loadGoalCalories();

        day.setRemainingCalories(goalCalories - eat_calories);
        dataManager.updateCalories(eat_calories, day.getRemainingCalories(), day.getDate());

        String remainingCalories = String.valueOf(day.getRemainingCalories());
        String eatCalories = String.valueOf(eat_calories);
        int checkLimit = checkLimit(day.getRemainingCalories());
        int color = getColor(checkLimit);
        if (diaryView != null) {
            if (checkLimit != 1 && day.getEatDailyCalories() != 0 && goalCalories != 0) { //if need dialog for limit
                diaryView.showDialog(remainingCalories, checkLimit);
            }
            diaryView.updateCalories(eatCalories, remainingCalories, color);
        }
    }

    @Override
    public void getGoalCalories() {
        int goalCalories = LocalDataBaseManager.loadGoalCalories();
        if (diaryView != null)
            diaryView.setGoalCalories(String.valueOf(goalCalories));
    }

    private int checkLimit(int remaining) {
        int checkLimit = 1;// if limit not reached

        if (remaining <= 300) checkLimit = 2; // if almost reach limit

        if (remaining <= 0) checkLimit = 3; // if limit reached

        if (remaining <= -500) checkLimit = 4; // if limit quite a a lot

        return checkLimit;
    }

    private int getColor(int checkLimit) {
        int color = Color.parseColor("#212121");

        if (checkLimit == 2) color = Color.parseColor("#7cab26");

        if (checkLimit == 3) color = Color.parseColor("#d30b0b");

        if (checkLimit == 4) color = Color.parseColor("#a40f0f");

        return color;
    }
}
