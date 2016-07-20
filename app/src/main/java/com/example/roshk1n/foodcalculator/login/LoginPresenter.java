package com.example.roshk1n.foodcalculator.login;

import com.facebook.login.widget.LoginButton;

/**
 * Created by roshk1n on 7/14/2016.
 */
public interface LoginPresenter {

    void setView(LoginView view);

    void loginWithApi(String login, String password);

    void checkLogin();

    void loginWithEmail(String email, String passwod);

    void loginFacebookListner(LoginButton loginButton);

}
