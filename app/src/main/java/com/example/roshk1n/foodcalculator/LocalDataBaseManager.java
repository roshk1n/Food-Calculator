package com.example.roshk1n.foodcalculator;

import com.example.roshk1n.foodcalculator.realm.DayRealm;
import com.example.roshk1n.foodcalculator.realm.FavoriteListRealm;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.ReminderReaml;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Reminder;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

public class LocalDataBaseManager {

    private DayRealm dayRealm;
    private FavoriteListRealm favoriteListRealm;
    private RealmList<ReminderReaml> remindersRealm;
    private final Realm realm = Realm.getDefaultInstance();

    public LocalDataBaseManager() {}

    public Day loadDayData(Date date) {
        Day day = new Day();

        for (int i = 0; i < getCurrentUserRealm().getDayRealms().size(); i++) {
            if(compareLongAndDate(getCurrentUserRealm().getDayRealms().get(i).getDate(),date)) {
                dayRealm = getCurrentUserRealm().getDayRealms().get(dayIsExist(date));
            }
        }

        if(dayRealm != null) {
            day = dayRealm.convertToModel();
        }
        return day;
    }

    public int dayIsExist(Date date) {
        for (int i = 0; i < getCurrentUserRealm().getDayRealms().size(); i++) {
            if(compareLongAndDate(getCurrentUserRealm().getDayRealms().get(i).getDate(),date)) {
                dayRealm = getCurrentUserRealm().getDayRealms().get(i);
                return i;
            }
        }
        return -1;
    }

    public void addFood(final Food food, final int indexDay) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dayRealm.getFoods().add(food.converToRealm());
            }
        });
    }

    public void createDay(final Long date) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DayRealm newDay = new DayRealm();
                newDay.setDate(date);
                newDay.setEatDailyCalories(0);
                newDay.setRemainingCalories(getCurrentUserRealm().getGoalCalories());
                getCurrentUserRealm().getDayRealms().add(newDay);
                dayRealm = getCurrentUserRealm().getDayRealms().last();
            }
        });

    }

    public void removeFood(final int indexRemove) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dayRealm.getFoods().get(indexRemove).deleteFromRealm();
            }
        });
    }

    public ArrayList<Food> loadFavoriteFood() {
        if(getCurrentUserRealm().getFavoriteList()!=null) {
            favoriteListRealm = getCurrentUserRealm().getFavoriteList();
        }
        ArrayList<Food> favoriteList = new ArrayList<>(); //convert to base model
        for (int i = 0; i < favoriteListRealm.getFoods().size(); i++) {
            favoriteList.add(favoriteListRealm.getFoods().get(i).converToModel());
        }
        return favoriteList;
    }

    public void addFavoriteFood(final Food food) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getCurrentUserRealm().getFavoriteList().getFoods().add(food.converToRealm());
            }
        });
    }

    public void removeFavoriteFoodDB(final int position) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                favoriteListRealm.getFoods().get(position).deleteFromRealm();
            }
        });
    }

    public void removeFavoriteFoodDB(String ndbno) {
        for(int i =0; i<getCurrentUserRealm().getFavoriteList().getFoods().size();i++) {
            if(ndbno.equals(getCurrentUserRealm().getFavoriteList().getFoods().get(i).getNdbno())) {
                final int finalI = i;
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        getCurrentUserRealm().getFavoriteList().getFoods().remove(finalI);
                    }
                });
            }
        }
    }

    private UserRealm getCurrentUserRealm() {
        return realm.where(UserRealm.class)
                .equalTo("email", Session.getInstance().getEmail())
                .findFirst();
    }

    private boolean compareLongAndDate(Long UserDate, Date date) {
        Date userDayDate = new Date(UserDate);
        return (userDayDate.getDate()== date.getDate()
                && userDayDate.getYear() == date.getYear()
                && userDayDate.getMonth()== date.getMonth());
    }

    public int loadGoalCalories() {
        return getCurrentUserRealm().getGoalCalories();
    }

    public void updateCalories(final int eat_calories, final int remainingCalories) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dayRealm.setEatDailyCalories(eat_calories);
                dayRealm.setRemainingCalories(remainingCalories);
            }
        });
    }

    public boolean isExistInFavotite(Food food) {
        return food.converToRealm().isExistIn(getCurrentUserRealm().getFavoriteList().getFoods());
    }

    // reminders
    public ArrayList<Reminder> loadReminders() {
        remindersRealm = getCurrentUserRealm().getReminders();
        ArrayList<Reminder> reminders = new ArrayList<>();
        for(int i = 0; i<remindersRealm.size();i++) {
            reminders.add(remindersRealm.get(i).convertToBase());
        }
        return reminders;
    }

    public void updateReminderState(final boolean check, final int position) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                remindersRealm.get(position).setState(check);
            }
        });
    }

    public void updateReminderTime(final int positionAdapter, final long date) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                remindersRealm.get(positionAdapter).setTime(date);
            }
        });
    }

    public boolean getRemindersState(int position) {
       return remindersRealm.get(position).getState();
    }

    public void saveReminders(ArrayList<Reminder> reminders) {
        remindersRealm = new RealmList<>();
        for (int i = 0; i<reminders.size();i++) {
            remindersRealm.add(reminders.get(i).convertToRealm());
        }
    }

    public Reminder getNotification(int positionAdapter) {
       return getCurrentUserRealm().getReminders().get(positionAdapter).convertToBase();
    }
}
