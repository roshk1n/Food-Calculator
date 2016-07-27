package com.example.roshk1n.foodcalculator.main.fragments.diary;

import android.support.v4.app.Fragment;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.realm.DayRealm;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by roshk1n on 7/19/2016.
 */
public class DiaryPresenterImpl implements DiaryPresenter,  DatePickerDialog.OnDateSetListener{

    private DiaryView diaryView;
    private Realm realm= Realm.getDefaultInstance();

    @Override
    public void setView(DiaryView view) {
        diaryView = view;
    }

    @Override
    public void showDatePicker(Fragment fragment) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setAccentColor(fragment.getActivity().getResources().getColor(R.color.colorPrimary));
        datePickerDialog.show( fragment.getActivity().getFragmentManager(), "DatePickerDialog" );
    }

    @Override
    public UserRealm getCurrentUserRealm() {
        final UserRealm userRealms = realm.where(UserRealm.class)
                .equalTo("email", Session.getInstance().getEmail())
                .findFirst();
        return userRealms;
    }

    @Override
    public RealmList<FoodRealm> getFoods(UserRealm user, Date date) {
        RealmList<FoodRealm> foodRealms = new RealmList<>();
        for (int i = 0; i < user.getDayRealms().size(); i++) {
            if(user.getDayRealms().get(i).getDate().getDate()== date.getDate()
                    && user.getDayRealms().get(i).getDate().getYear() == date.getYear()
                    && user.getDayRealms().get(i).getDate().getMonth()== date.getMonth()) {

                foodRealms = user.getDayRealms().get(i).getFoods();
                calculateCaloriesAdd(i);
            }
        }
        return foodRealms;
    }

    @Override
    public void removeFood(int index, Date date) {

        realm.beginTransaction();
        int day = 0;
        for (int i = 0; i < getCurrentUserRealm().getDayRealms().size(); i++) {
            if(getCurrentUserRealm().getDayRealms().get(i).getDate().getDate()==date.getDate()
                    && getCurrentUserRealm().getDayRealms().get(i).getDate().getMonth()==date.getMonth()
                    && getCurrentUserRealm().getDayRealms().get(i).getDate().getYear()==date.getYear()) {
                day=i;

            }
        }
        realm.commitTransaction();
        calculateCaloriesRemove(day,index);
        realm.beginTransaction();
        getCurrentUserRealm().getDayRealms().get(day).getFoods().get(index).deleteFromRealm();
        realm.commitTransaction();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        Date date = new Date();
        date.setMonth(monthOfYear);
        date.setDate(dayOfMonth);
        date.setYear(year-1900); //TODO: unreal )

        diaryView.setDate(date);
    }

    private void calculateCaloriesRemove(int day, int index) {
        realm.beginTransaction();
        DayRealm infoDay = getCurrentUserRealm().getDayRealms().get(day);

        infoDay.setEatDailyCalories(infoDay.getEatDailyCalories()
                - Integer.valueOf(infoDay.getFoods().get(index).getNutrients().get(1).getValue()));
        infoDay.setRemainingCalories(infoDay.getGoalCalories() - infoDay.getEatDailyCalories());
        realm.commitTransaction();
        diaryView.updateCalories(infoDay.getGoalCalories(),infoDay.getEatDailyCalories(),infoDay.getRemainingCalories());
    }
    private void calculateCaloriesAdd(int day) {
        realm.beginTransaction();
        DayRealm infoDay = getCurrentUserRealm().getDayRealms().get(day);
        int eat_calories=0;
        for (int i = 0; i < infoDay.getFoods().size(); i++) {
            eat_calories += Integer.valueOf(infoDay.getFoods().get(i).getNutrients().get(1).getValue());
        }
        infoDay.setEatDailyCalories(eat_calories);
        infoDay.setRemainingCalories(infoDay.getGoalCalories() - infoDay.getEatDailyCalories());
        realm.commitTransaction();
        diaryView.updateCalories(infoDay.getGoalCalories(),infoDay.getEatDailyCalories(),infoDay.getRemainingCalories());
    }
}
