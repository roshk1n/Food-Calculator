package com.example.roshk1n.foodcalculator.presenters;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.roshk1n.foodcalculator.CallbackLocalManager;
import com.example.roshk1n.foodcalculator.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.Views.LoginView;
import com.example.roshk1n.foodcalculator.remoteDB.CallbackFirebase;
import com.example.roshk1n.foodcalculator.remoteDB.FirebaseHelper;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User;
import com.example.roshk1n.foodcalculator.utils.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPresenterImpl implements LoginPresenter, CallbackFirebase, CallbackLocalManager {

    private final static String TAG = "MyLog";
    private LocalDataBaseManager localDataBaseManager = new LocalDataBaseManager();
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
        FirebaseHelper.setmAuth(FirebaseAuth.getInstance());
        FirebaseHelper.setmAuthListner(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    FirebaseHelper.setmFirebaseUser(FirebaseHelper.getmAuth().getCurrentUser());
                    Session.startSession();
                    Session.getInstance().setEmail(FirebaseHelper.getmAuth().getCurrentUser().getEmail());
                    Session.getInstance().setFullname(FirebaseHelper.getmAuth().getCurrentUser().getDisplayName());
                    Session.getInstance().setUrlPhoto(FirebaseHelper.getmAuth().getCurrentUser().toString());
                    loginVew.navigateToHome();
                }
                }
        });
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
                if(Utils.isConnectNetwork(loginVew.getContext())) {
                    FirebaseHelper.logInWithEmail(email, password, this);

                } else {
                    localDataBaseManager.loginUser(email,password,this);
                }
            }
        }
    }

    @Override
    public void loginSuccessful() {
        Session.startSession();
        Session.getInstance().setEmail(FirebaseHelper.getmFirebaseUser().getEmail());
        Session.getInstance().setFullname(FirebaseHelper.getmFirebaseUser().getDisplayName());
        Session.getInstance().setUrlPhoto(FirebaseHelper.getmFirebaseUser().getPhotoUrl().toString());
        loginVew.navigateToHome();
    }

    @Override
    public void LoginRealmSuccess(User user) {
        if (user != null) {
            Session.startSession();
            Session.getInstance().setEmail(user.getEmail());
            Session.getInstance().setFullname(user.getFullname());
            Session.getInstance().setUrlPhoto(user.getPhotoUrl());
            loginVew.navigateToHome();
        }
    }

    @Override
    public void showToast(String text) {
        loginVew.showToast("Authentication failed. Try again please!");
    }
}