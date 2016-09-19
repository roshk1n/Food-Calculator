package com.example.roshk1n.foodcalculator.manageres;

import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.interfaces.OnCompleteCallback;
import com.example.roshk1n.foodcalculator.realmModel.DayRealm;
import com.example.roshk1n.foodcalculator.realmModel.FoodRealm;
import com.example.roshk1n.foodcalculator.realmModel.ReminderReaml;
import com.example.roshk1n.foodcalculator.realmModel.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.*;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmList;

public class LocalDataBaseManager {
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

    public static Day loadDayData(Calendar date) {
        Day day = new Day();
        boolean checkDay = false;
        if (dayRealm != null) {
            for (int i = 0; i < getCurrentUserRealm().getDayRealms().size(); i++) {
                if (compareLongAndDate(getCurrentUserRealm().getDayRealms().get(i).getDate(), date)) {
                    dayRealm = getCurrentUserRealm().getDayRealms().get(i);
                    checkDay = true;
                }
            }
        }
        if (checkDay) {
            day = new Day(dayRealm);
        } else {
            dayRealm = null;
        }

        return day;
    }

    public static int dayIsExist(Calendar date) {
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

    public static int loadGoalCalories() {
        return getCurrentUserRealm().getGoalCalories();
    }

    public static void updateCalories(final int eat_calories, final int remainingCalories) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (dayRealm != null) {
                    dayRealm.setEatDailyCalories(eat_calories);
                    dayRealm.setRemainingCalories(remainingCalories);
                }
            }
        });
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
                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(day.getDate());
                int i = dayIsExist(date);
                DayRealm newDayReam = new DayRealm(day);
                if (i != -1) {
                    getCurrentUserRealm().getDayRealms().get(i).getFoods().deleteAllFromRealm();
                    getCurrentUserRealm().getDayRealms().get(i).getFoods().addAll(newDayReam.getFoods());
                    dayRealm = getCurrentUserRealm().getDayRealms().get(i);
                } else {
                    if (newDayReam.getFoods().size() > 0) {
                        getCurrentUserRealm().getDayRealms().add(newDayReam);
                        dayRealm = getCurrentUserRealm().getDayRealms().last();
                    }
                }
            }
        });
    }

    public static void checkLocalUser(OnCompleteCallback callback) {
        UserRealm user = realm.where(UserRealm.class)
                .equalTo("email", Session.getInstance().getEmail())
                .findFirst();
        if (user == null) {
            final UserRealm userRealm = new UserRealm(Session.getInstance().getFullname(),
                    Session.getInstance().getEmail(), Session.getInstance().getUrlPhoto());

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(userRealm);
                }
            });
        }
        callback.success();
    }

    public static void checkFacebookLocalUser(User user, final OnCompleteCallback callback) {
        UserRealm userRealm = realm.where(UserRealm.class)
                .equalTo("email", user.getEmail())
                .findFirst();
        if (userRealm == null) {
            final UserRealm userR = new UserRealm(user.getFullname(), user.getEmail(), user.getPhotoUrl(),
                    user.getAge(), user.getSex());

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(userR);
                    callback.success();
                }
            });
        }
    }

    public static String getLocalUserImage() {
        return getCurrentUserRealm().getPhotoUrl();
    }

    private static UserRealm getCurrentUserRealm() {
        return realm.where(UserRealm.class)
                .equalTo("email", Session.getInstance().getEmail())
                .findFirst();
    }

    private static boolean compareLongAndDate(Long UserDate, Calendar date) {
        Calendar userDayDate = Calendar.getInstance();
        userDayDate.setTimeInMillis(UserDate);
        return (userDayDate.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)
                && userDayDate.get(Calendar.YEAR) == date.get(Calendar.YEAR)
                && userDayDate.get(Calendar.MONTH) == date.get(Calendar.MONTH));
    }
}
