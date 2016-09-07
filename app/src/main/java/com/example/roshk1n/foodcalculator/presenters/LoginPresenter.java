package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.views.LoginView;
import com.facebook.login.widget.LoginButton;

public interface LoginPresenter {

    void setView(LoginView view);

    void checkLogin();

    void loginFacebookListner(LoginButton loginButton);

    void login(String email, String password);

}
