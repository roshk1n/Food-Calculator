package com.example.roshk1n.foodcalculator.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by roshk1n on 7/22/2016.
 */
public class UserRealm extends RealmObject {
    @PrimaryKey
    private String email;
    private String fullname;
    private String password;
    private String photoUrl;
    private FavoriteListRealm favoriteList;
    private RealmList<ReminderReaml> reminders = new RealmList<ReminderReaml>();
    private RealmList<DayRealm> dayRealms = new RealmList<DayRealm>();

    public UserRealm() { }

    public UserRealm(String fullname, String email, String password, String photo) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.photoUrl = photo;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public FavoriteListRealm getFavoriteList() {
        return favoriteList;
    }

    public void setFavoriteList(FavoriteListRealm favoriteList) {
        this.favoriteList = favoriteList;
    }

    public RealmList<DayRealm> getDayRealms() {
        return dayRealms;
    }

    public void setDayRealms(RealmList<DayRealm> dayRealms) {
        this.dayRealms = dayRealms;
    }
}
