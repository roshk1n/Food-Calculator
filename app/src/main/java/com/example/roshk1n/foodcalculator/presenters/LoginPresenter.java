package com.example.roshk1n.foodcalculator.presenters;

/**
 * Created by roshk1n on 7/14/2016.
 */
public interface LoginPresenter {
    interface OnLoginFinishedListener {
        void onUsernameError();

        void onPasswordError();

        void onSuccess();
    }

    void login(String login, String password, OnLoginFinishedListener listener);
}
