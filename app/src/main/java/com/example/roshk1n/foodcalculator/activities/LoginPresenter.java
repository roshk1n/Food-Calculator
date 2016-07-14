package com.example.roshk1n.foodcalculator.activities;

/**
 * Created by Mykhailo Nester on 7/13/16.
 */
public class LoginPresenter {

    private LoginView loginView;

    public LoginPresenter() {}

    public void setView(LoginView loginView) {
        this.loginView = loginView;
    }

    public void login() {


        loginView.updateUi();

    }
}
