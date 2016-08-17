package com.example.roshk1n.foodcalculator.presenters;

import android.content.Context;
import android.text.TextUtils;

import com.example.roshk1n.foodcalculator.interfaces.DataCallback;
import com.example.roshk1n.foodcalculator.DataManaget;
import com.example.roshk1n.foodcalculator.Views.LoginView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginPresenterImpl implements LoginPresenter, DataCallback {

    private final static String TAG = "MyLog";
    private DataManaget dataManager = new DataManaget(this);
    private CallbackManager callbackManager;
    private LoginView loginVew;

    public LoginPresenterImpl() {
    }

    @Override
    public void setView(LoginView view) {
        loginVew = view;
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    @Override
    public void checkLogin() {
        dataManager.checkLogin();
    }

    @Override
    public void loginFacebookListner(LoginButton loginButton) {
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginVew.showToast("Login attempt succeed.");
            }

            @Override
            public void onCancel() {
                loginVew.showToast("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                loginVew.showToast("Login attempt failed.");
            }
        });
    }

    @Override
    public void login(String email, String password) {
        boolean error = false;
        if (loginVew != null) {
            //            TODO: email validation
            if (TextUtils.isEmpty(email)) {
                loginVew.setEmailError();
                error = true;
            }

            if (TextUtils.isEmpty(password)) {
                loginVew.setPasswordError();
                error = true;
            }

            if (!error) {
                dataManager.loginWithLogin(email,password);
            }
        }
    }

    @Override
    public void showToast(String text) {
        loginVew.showToast("Authentication failed. Try again please!");
    }

    @Override
    public void navigateToHome() {
        loginVew.navigateToHome();
    }

    @Override
    public Context getContext() {
        return loginVew.getContext();
    }
}