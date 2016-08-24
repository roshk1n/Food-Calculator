package com.example.roshk1n.foodcalculator.presenters;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.roshk1n.foodcalculator.interfaces.DataLoginCallback;
import com.example.roshk1n.foodcalculator.DataManager;
import com.example.roshk1n.foodcalculator.Views.LoginView;
import com.example.roshk1n.foodcalculator.interfaces.OnCompleteCallback;
import com.example.roshk1n.foodcalculator.interfaces.UserProfileCallback;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class LoginPresenterImpl implements LoginPresenter, DataLoginCallback {

    private final static String TAG = "MyLog";
    private DataManager dataManager = new DataManager(this);
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
            public void onSuccess(final LoginResult loginResult) {
                final GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                dataManager.loginFacebook(loginResult.getAccessToken(),object, new OnCompleteCallback() {
                                    @Override
                                    public void success() {
                                        loginVew.loginSuccessFacebook();
                                    }
                                });
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
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
            if (TextUtils.isEmpty(email)) {
                loginVew.setEmailError();
                error = true;
            }

            if (TextUtils.isEmpty(password)) {
                loginVew.setPasswordError();
                error = true;
            }

            if (!error) {
                dataManager.login(email, password);
            }
        }
    }

    @Override
    public void showToast(String text) {
        loginVew.showToast(text);
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