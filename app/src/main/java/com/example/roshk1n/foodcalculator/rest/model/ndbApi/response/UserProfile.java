package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import com.example.roshk1n.foodcalculator.realm.UserRealm;

/**
 * Created by roshk1n on 8/12/2016.
 */
public class UserProfile {

    private String photoUrl;
    private String email;
    private String password;
    private String fullname;
    private int age;
    private int height;
    private int weight;
    private String sex;
    private String active_level;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getSex() {
        return sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getActive_level() {
        return active_level;
    }

    public void setActive_level(String active_level) {
        this.active_level = active_level;
    }

    public UserRealm convertToRealm() {
        UserRealm userRealm = new UserRealm();
        userRealm.setEmail(this.getEmail());
        userRealm.setPassword(this.getPassword());
        userRealm.setFullname(this.getFullname());
        userRealm.setWeight(this.getWeight());
        userRealm.setHeight(this.getHeight());
        userRealm.setAge(this.getAge());
        userRealm.setSex(this.getSex());
        userRealm.setActiveLevel(this.getActive_level());
        return userRealm;
    }
}
