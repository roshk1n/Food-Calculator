package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.Views.LoginView;
import com.facebook.login.widget.LoginButton;

/**
 * Created by roshk1n on 7/14/2016.
 */
public interface LoginPresenter {

    void setView(LoginView view);

    void checkLogin();

    void loginWithEmail(String email, String passwod);

    void loginFacebookListner(LoginButton loginButton);

    void loginRealm(String email, String password);

}
