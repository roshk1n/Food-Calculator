package com.example.roshk1n.foodcalculator;


import com.example.roshk1n.foodcalculator.interfaces.LocalManagerCallback;
import com.example.roshk1n.foodcalculator.interfaces.OnCompleteCallback;
import com.example.roshk1n.foodcalculator.realm.DayRealm;
import com.example.roshk1n.foodcalculator.realm.FavoriteListRealm;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.ReminderReaml;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.*;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User;


import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

public class LocalDataBaseManager {

    private static FavoriteListRealm favoriteListRealm;
    private static RealmList<ReminderReaml> remindersRealm;
    private static final Realm realm = Realm.getDefaultInstance();
    private static DayRealm dayRealm;

    public LocalDataBaseManager() {
    }

    public static void createUser(String fullname, String email, String image) {
        final UserRealm userRealm = new UserRealm(fullname, email, image);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(userRealm);
            }
        });
    }

    public static void loginUser(String email, String password, LocalManagerCallback callback) {
  /*      User user;
        UserRealm userRealms = realm.where(UserRealm.class)
                .equalTo("email", email)
                .equalTo("password", password).findFirst();

        if(userRealms != null) {
            user = new User(userRealms);
            Session.startSession();
            Session.getInstance().setEmail(user.getEmail());
            Session.getInstance().setFullname(user.getFullname());
            Session.getInstance().setUrlPhoto(user.getPhotoUrl());
            callback.loginSuccessful();
        } else {
            callback.showToast("Authentication failed. Try again please!");
        }*/
    }

    public static Day loadDayData(Date date) {
        Day day = new Day();
        boolean checkDay = false;
        for (int i = 0; i < getCurrentUserRealm().getDayRealms().size(); i++) {
            if (compareLongAndDate(getCurrentUserRealm().getDayRealms().get(i).getDate(), date)) { //TODO dayIsExist
                dayRealm = getCurrentUserRealm().getDayRealms().get(i);
                checkDay = true;
            }
        }

        if (checkDay) {
            day = new Day(dayRealm);
        }
        return day;
    }

    public static int dayIsExist(Date date) {
        for (int i = 0; i < getCurrentUserRealm().getDayRealms().size(); i++) {
            if (compareLongAndDate(getCurrentUserRealm().getDayRealms().get(i).getDate(), date)) {
                dayRealm = getCurrentUserRealm().getDayRealms().get(i);
                return i;
            }
        }
        return -1;
    }

    public static void addFood(final Food food) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dayRealm.getFoods().add(new FoodRealm(food));
            }
        });
    }

    public static void createDay(final Long date) {
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

    public static void removeFood(final int indexRemove) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dayRealm.getFoods().get(indexRemove).deleteFromRealm();
            }
        });
    }

    public static ArrayList<Food> loadFavoriteFood() {
        if (getCurrentUserRealm().getFavoriteList() != null) {
            favoriteListRealm = getCurrentUserRealm().getFavoriteList();
        }

        ArrayList<Food> favoriteList = new ArrayList<>(); //convert to base model
        for (FoodRealm foodRealm : favoriteListRealm.getFoods()) {
            favoriteList.add(new Food(foodRealm));
        }
        return favoriteList;
    }

    public static void addFavoriteFood(final Food food) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getCurrentUserRealm().getFavoriteList().getFoods().add(new FoodRealm(food));
            }
        });
    }

    public static void removeFavoriteFoodDB(final int position) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                favoriteListRealm.getFoods().get(position).deleteFromRealm();
            }
        });
    }

    public static void removeFavoriteFoodDB(String ndbno) {
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

    public static int loadGoalCalories() {
        return getCurrentUserRealm().getGoalCalories();
    }

    public static void updateCalories(final int eat_calories, final int remainingCalories) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if(dayRealm != null) {
                    dayRealm.setEatDailyCalories(eat_calories);
                    dayRealm.setRemainingCalories(remainingCalories);
                }
            }
        });
    }

    public static boolean isExistInFavotite(Food food) {
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

    public static Reminder getNotification(int positionAdapter) {
        remindersRealm = getCurrentUserRealm().getReminders();
        return new Reminder(remindersRealm.get(positionAdapter));
    }

    public static User loadUser() {
        return new User(getCurrentUserRealm());
    }

    public static void updateUserProfile(final User user) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getCurrentUserRealm().setFullname(user.getFullname());
                getCurrentUserRealm().setWeight(user.getWeight());
                getCurrentUserRealm().setHeight(user.getHeight());
                getCurrentUserRealm().setAge(user.getAge());
                getCurrentUserRealm().setPhotoUrl(user.getPhotoUrl());
                getCurrentUserRealm().setSex(user.getSex());
                getCurrentUserRealm().setActiveLevel(user.getActiveLevel());
                getCurrentUserRealm().setGoalCalories(user.getGoalCalories());
                getCurrentUserRealm().setEmail(user.getEmail());
            }
        });
    }

    public static void updateDays(final Day day) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int i = dayIsExist(new Date(day.getDate()));
                DayRealm newDayReam = new DayRealm(day);
                if(i!=-1) {
                    getCurrentUserRealm().getDayRealms().get(i).deleteFromRealm();
                    getCurrentUserRealm().getDayRealms().add(newDayReam);
                    dayRealm = getCurrentUserRealm().getDayRealms().last();
                } else {
                    getCurrentUserRealm().getDayRealms().add(newDayReam);
                    dayRealm = getCurrentUserRealm().getDayRealms().last();
                }
            }
        });
    }

    public static void checkLocalUser(final OnCompleteCallback callback) {
        UserRealm user = realm.where(UserRealm.class)
                .equalTo("email",Session.getInstance().getEmail())
                .findFirst();
        if(user == null) {
            final UserRealm userRealm = new UserRealm(Session.getInstance().getFullname(),
                    Session.getInstance().getEmail(),Session.getInstance().getUrlPhoto());

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(userRealm);
                }
            });
        }
        callback.success();
    }

    public static void addLocalUserImage(final String image) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getCurrentUserRealm().setPhotoUrl(image);
            }
        });
    }

    private static UserRealm getCurrentUserRealm() {
        return realm.where(UserRealm.class)
                .equalTo("email", Session.getInstance().getEmail())
                .findFirst();
    }

    private static boolean compareLongAndDate(Long UserDate, Date date) {
        Date userDayDate = new Date(UserDate);
        return (userDayDate.getDate() == date.getDate()
                && userDayDate.getYear() == date.getYear()
                && userDayDate.getMonth() == date.getMonth());
    }

    public static String getLocalUserImage() {
        return getCurrentUserRealm().getPhotoUrl();
    }
}
