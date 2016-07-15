package com.example.roshk1n.foodcalculator.activities.presenters;

import android.view.View;

import com.example.roshk1n.foodcalculator.activities.views.LoginView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by roshk1n on 7/14/2016.
 */
public interface LoginPresenter {

    void setView(LoginView view);

    void login(String login, String password);

    void checkLogin();

    void loginWithEmail(String email, String passwod);
}
