package com.example.roshk1n.foodcalculator.activities.presenters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.roshk1n.foodcalculator.ManageLoginApi;
import com.example.roshk1n.foodcalculator.MyApplication;
import com.example.roshk1n.foodcalculator.activities.MainActivity;
import com.example.roshk1n.foodcalculator.activities.views.LoginView;
import com.example.roshk1n.foodcalculator.remoteDB.FirebaseHelper;
import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by roshk1n on 7/14/2016.
 */
public class LoginPresenterImpl implements LoginPresenter{

    private static String TAG = "MyLog";
    private MyApplication myApplication;
    private LoginView loginVew;

    public LoginPresenterImpl() {}

    @Override
    public void setView(LoginView view) {
        loginVew = view;
    }

    @Override
    public void loginWithApi(String email, String password) {
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
                ManageLoginApi.login("roshk1n.ua@gmail.com", "132132132");
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
                OnCompleteListener onCompleteListener = new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(!task.isSuccessful()) {
                            loginVew.failedAuth();
                        }
                        Log.d(TAG, "signInWithEmail:" + task.isSuccessful());
                    }
                };
                FirebaseHelper.logInWhithEmail(email,password,onCompleteListener);
            }
        }
    }
}










/*


 public static void registerUser(String f_namem, String l_name, String u_email, String u_password) {

        com.example.roshk1n.foodcalculator.rest.model.loginApi.RegistrationUser user = new com.example.roshk1n.foodcalculator.rest.model.loginApi.RegistrationUser("Oleh", "Roshka", "roshk1n.ua@gmail.com", "132132132");
        RestClient restClient = new RestClient();

        restClient.getLoginApi().registrationUser(user, new Callback<RegistrationResponse>() {
            @Override
            public void success(RegistrationResponse responeRegister, Response response) {

            }
            @Override
            public void failure(RetrofitError error) {

            }
        });
    }*/
