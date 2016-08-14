package com.example.roshk1n.foodcalculator.presenters;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.roshk1n.foodcalculator.LoginApiManager;
import com.example.roshk1n.foodcalculator.MyApplication;
import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.Views.LoginView;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.remoteDB.FirebaseHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.realm.Realm;

public class LoginPresenterImpl implements LoginPresenter {

    private final static String TAG = "MyLog";
    private MyApplication myApplication;
    private CallbackManager callbackManager;
    private LoginView loginVew;

    public LoginPresenterImpl() {}

    @Override
    public void setView(LoginView view) {
        loginVew = view;
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    @Override
    public void loginWithApi(String email, String password) {
        boolean error = false;
        if(loginVew != null) {
//            TODO: email validation
            if (TextUtils.isEmpty(email)) {
                loginVew.setEmailError();
                error = true;
            }
//              TODO: min 6 symbols
            if (TextUtils.isEmpty(password)) {
                loginVew.setPasswordError();
                error = true;
            }

            if (!error){
                LoginApiManager.login("roshk1n.ua@gmail.com", "132132132");
//TODO:
//                restClient.getLoginService().login(loginUser, new Callback<com.example.roshk1n.foodcalculator.rest.model.loginApi.response.LoginResponse>() {
//                    @Override
//                    public void success(com.example.roshk1n.foodcalculator.rest.model.loginApi.response.LoginResponse loginResponse, Response response) {
//                        loginResponse.getData();
//                    }
//                    @Override
//                    public void failure(RetrofitError error) {
//
//                    }
//                });
            }
        }
    }

    @Override
    public void checkLogin() {
        FirebaseHelper.setmAuth(FirebaseAuth.getInstance());
        FirebaseHelper.setmAuthListner(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    FirebaseHelper.setmFirebaseUser(FirebaseHelper.getmAuth().getCurrentUser());
                    loginVew.navigateToHome();
                }
            }
        });
    }

    @Override
    public void loginWithEmail(String email, String password) {

        boolean error = false;
        if(loginVew != null) {
            if (TextUtils.isEmpty(email)) {
                loginVew.setEmailError();
                error = true;
            }

            if (TextUtils.isEmpty(password)) {
                loginVew.setPasswordError();
                error = true;
            }

            if (!error){

                FirebaseHelper.logInWhithEmail(email, password, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                            if(!task.isSuccessful()) {
                                loginVew.showToast("Authentication failed. Try again please!");
                            } else {
                                loginVew.navigateToHome();
                            }
                            Log.d(TAG, "signInWithEmail:" + task.isSuccessful());
                        }
                });
            }
        }
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
    public void loginRealm(String email, String password) {

        Realm realm = Realm.getDefaultInstance();
        UserRealm userRealms = realm.where(UserRealm.class)
                .equalTo("email",email)
                .equalTo("password",password).findFirst();

        if(userRealms!= null) {
            Session.startSession();
            Session.getInstance().setEmail(userRealms.getEmail());
            Session.getInstance().setFullname(userRealms.getFullname());
            Session.getInstance().setUrlPhoto(userRealms.getPhotoUrl());
            loginVew.navigateToHome();
        }
    }
}