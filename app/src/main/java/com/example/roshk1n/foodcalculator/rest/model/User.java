package com.example.roshk1n.foodcalculator.rest.model;

import android.util.Log;

import com.firebase.client.Firebase;

/**
 * Created by AndroidBash on 11/05/16
 */
public class User {

    private String id;
    private String fullname;
    private String email;
    private String password;
    private String photoUrl;


    public User() {
    }

    public User(String id, String fullname, String email, String password, String photoUrl) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.photoUrl = photoUrl;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void saveUser() {
        Firebase myFirebaseRef = new Firebase("https://food-calculator.firebaseio.com/");
        myFirebaseRef = myFirebaseRef.child("users");
        Firebase firebasepush = myFirebaseRef.push();
        firebasepush.setValue(this);
        Log.d("MyLog",firebasepush.getKey());
    }

}
