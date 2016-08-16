package com.example.roshk1n.foodcalculator;

import com.example.roshk1n.foodcalculator.realm.DayRealm;
import com.example.roshk1n.foodcalculator.realm.FavoriteListRealm;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.ReminderReaml;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.*;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User;
import com.example.roshk1n.foodcalculator.utils.Utils;


import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

public class LocalDataBaseManager {

    private DayRealm dayRealm;
    private FavoriteListRealm favoriteListRealm;
    private RealmList<ReminderReaml> remindersRealm;
    private CallbackLocalManager callbackLocal;
    private final Realm realm = Realm.getDefaultInstance();

    public LocalDataBaseManager() {
    }

    public LocalDataBaseManager(CallbackLocalManager callbackLocalManager) {
        this.callbackLocal = callbackLocalManager;
    }

    public void loginUser(String email, String password) {
        User user;
        UserRealm userRealms = realm.where(UserRealm.class)
                .equalTo("email", email)
                .equalTo("password", password).findFirst();

        if(userRealms != null) {
            user = new User(userRealms);
            Session.startSession();
            Session.getInstance().setEmail(user.getEmail());
            Session.getInstance().setFullname(user.getFullname());
            Session.getInstance().setUrlPhoto(user.getPhotoUrl());
            callbackLocal.loginSuccessful();
        } else {
            callbackLocal.showToast("Authentication failed. Try again please!");
        }
    }

    public Day loadDayData(Date date) {
        Day day = new Day();
        boolean checkDay = false;
        for (int i = 0; i < getCurrentUserRealm().getDayRealms().size(); i++) {
            if (compareLongAndDate(getCurrentUserRealm().getDayRealms().get(i).getDate(), date)) {
                dayRealm = getCurrentUserRealm().getDayRealms().get(i);
                checkDay = true;
            }
        }

        if (checkDay) {
            day = new Day(dayRealm);
        }
        return day;
    }

    public int dayIsExist(Date date) {
        for (int i = 0; i < getCurrentUserRealm().getDayRealms().size(); i++) {
            if (compareLongAndDate(getCurrentUserRealm().getDayRealms().get(i).getDate(), date)) {
                dayRealm = getCurrentUserRealm().getDayRealms().get(i);
                return i;
            }
        }
        return -1;
    }

    public void addFood(final Food food) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dayRealm.getFoods().add(new FoodRealm(food));
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
        if (getCurrentUserRealm().getFavoriteList() != null) {
            favoriteListRealm = getCurrentUserRealm().getFavoriteList();
        }
        ArrayList<Food> favoriteList = new ArrayList<>(); //convert to base model
        for (FoodRealm foodRealm : favoriteListRealm.getFoods()) {
            favoriteList.add(new Food(foodRealm));
        }
        return favoriteList;
    }

    public void addFavoriteFood(final Food food) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getCurrentUserRealm().getFavoriteList().getFoods().add(new FoodRealm(food));
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
        for (int i = 0; i < getCurrentUserRealm().getFavoriteList().getFoods().size(); i++) {
            if (ndbno.equals(getCurrentUserRealm().getFavoriteList().getFoods().get(i).getNdbno())) {
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
        return (userDayDate.getDate() == date.getDate()
                && userDayDate.getYear() == date.getYear()
                && userDayDate.getMonth() == date.getMonth());
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
        return new FoodRealm(food).isExistIn(getCurrentUserRealm().getFavoriteList().getFoods());
    }

    public ArrayList<Reminder> loadReminders() {
        remindersRealm = getCurrentUserRealm().getReminders();
        ArrayList<Reminder> reminders = new ArrayList<>();
        for (ReminderReaml reminderReaml : remindersRealm) {
            reminders.add(new Reminder(reminderReaml));
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

    public void saveReminders(final ArrayList<Reminder> reminders) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Reminder reminder : reminders) {
                    remindersRealm.add(new ReminderReaml(reminder));
                }
            }
        });
    }

    public Reminder getNotification(int positionAdapter) {
        remindersRealm = getCurrentUserRealm().getReminders();
        return new Reminder(remindersRealm.get(positionAdapter));
    }

    public com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User loadUser() {
        return new User(getCurrentUserRealm());
    }

    public void updateUserProfile(final User user) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getCurrentUserRealm().setFullname(user.getFullname());
                getCurrentUserRealm().setWeight(user.getWeight());
                getCurrentUserRealm().setHeight(user.getHeight());
                getCurrentUserRealm().setAge(user.getAge());
                getCurrentUserRealm().setEmail(user.getEmail());
                getCurrentUserRealm().setPhotoUrl(user.getPhotoUrl());
                getCurrentUserRealm().setSex(user.getSex());
                getCurrentUserRealm().setActiveLevel(user.getActiveLevel());
                getCurrentUserRealm().setGoalCalories(user.getGoalCalories());
            }
        });
    }
}
