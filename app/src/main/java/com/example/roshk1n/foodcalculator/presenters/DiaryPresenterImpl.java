package com.example.roshk1n.foodcalculator.presenters;

import android.graphics.Color;
import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.Views.DiaryView;
import com.example.roshk1n.foodcalculator.presenters.DiaryPresenter;
import com.example.roshk1n.foodcalculator.realm.DayRealm;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

public class DiaryPresenterImpl implements DiaryPresenter {

    private DiaryView diaryView;
    private final Realm realm = Realm.getDefaultInstance();

    @Override
    public void setView(DiaryView view) {
        diaryView = view;
    }

    @Override
    public UserRealm getCurrentUserRealm() {
        return realm.where(UserRealm.class)
                .equalTo("email", Session.getInstance().getEmail())
                .findFirst();
    }

    @Override
    public RealmList<FoodRealm> getFoods(Date date) {
        RealmList<FoodRealm> foodRealms = new RealmList<>();

        for (int i = 0; i < getCurrentUserRealm().getDayRealms().size(); i++) {

            if(compareLongAndDate(getCurrentUserRealm().getDayRealms().get(i).getDate(),date)) {
                foodRealms = getCurrentUserRealm().getDayRealms().get(i).getFoods();
            }
        }
        return foodRealms;
    }

    @Override
    public void removeFood(final int index, Date date) {
        int day = 0;

        for (int i = 0; i < getCurrentUserRealm().getDayRealms().size(); i++) {
            if(compareLongAndDate(getCurrentUserRealm().getDayRealms().get(i).getDate(),date)) {
                day=i;
            }
        }

        final int finalDay = day;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getCurrentUserRealm().getDayRealms().get(finalDay).getFoods().get(index).deleteFromRealm();
            }
        });
    }

    @Override
    public String dateToString(Date date) {
        SimpleDateFormat format1 = new SimpleDateFormat();
        format1.applyPattern("EEEE, dd MMMM");
        return format1.format(date);
    }

    @Override
    public void calculateCalories(Date date) {

        for (int i = 0; i < getCurrentUserRealm().getDayRealms().size(); i++) {

            if(compareLongAndDate(getCurrentUserRealm().getDayRealms().get(i).getDate(),date)) {
                final DayRealm infoDay = getCurrentUserRealm().getDayRealms().get(i);

                int eat_calories=0;
                for (int j = 0; j < infoDay.getFoods().size(); j++) {
                    eat_calories += Integer.valueOf(infoDay.getFoods().get(j).getNutrients().get(1).getValue());
                }

                final int finalEat_calories = eat_calories;
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        infoDay.setEatDailyCalories(finalEat_calories);
                        infoDay.setRemainingCalories(getCurrentUserRealm().getGoalCalories() - infoDay.getEatDailyCalories());
                    }
                });
                String eat =  String.valueOf(infoDay.getEatDailyCalories());
                String remaining =  String.valueOf(infoDay.getRemainingCalories());
                int checkLimit = checkLimit(infoDay.getRemainingCalories());
                int color = getColor(checkLimit);
                diaryView.updateCalories(eat,remaining,checkLimit,color);
            }
        }
    }

    @Override
    public void getGoalCalories() {
        diaryView.setGoalCalories(String.valueOf(getCurrentUserRealm().getGoalCalories()));
    }

    private boolean compareLongAndDate(Long UserDate, Date date) {

        Date userDayDate = new Date(UserDate);
        return (userDayDate.getDate()== date.getDate()
                && userDayDate.getYear() == date.getYear()
                && userDayDate.getMonth()== date.getMonth());
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
