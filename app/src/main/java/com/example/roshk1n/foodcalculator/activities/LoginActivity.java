package com.example.roshk1n.foodcalculator.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.Localization;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.presenters.LoginPresenterImpl;
import com.example.roshk1n.foodcalculator.utils.Utils;
import com.example.roshk1n.foodcalculator.views.LoginView;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class LoginActivity extends Activity implements LoginView, View.OnFocusChangeListener {
    private final static String TAG = LoginActivity.class.getSimpleName();
    private LoginPresenterImpl loginPresenter;
    private TextInputLayout emailEt;
    private TextInputLayout passwordEt;
    private LoginButton btnLogInFacebook;
    private ProgressDialog loginProgress;

    //  TODO: The reason why you return to login screen after onBackPressed in main - Memory leak
    //  http://www.theshiftingbit.com/Fixing-Memory-Leaks-in-Android-Studio
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "LoginActivity OnCreate");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        initUI();
        Localization.setLocale(getContext());

        emailEt.setOnFocusChangeListener(this);
        passwordEt.setOnFocusChangeListener(this);

        loginPresenter = new LoginPresenterImpl();
        loginPresenter.setView(this);
        loginPresenter.checkLogin();

        btnLogInFacebook.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        btnLogInFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.loginFacebookListener(btnLogInFacebook);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "activityResult");
        loginProgress.setMessage(getString(R.string.wait_please));
        loginProgress.show();
        loginPresenter.getCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setEmailError() {
        emailEt.setErrorEnabled(true);
        Log.d(TAG, "errorEmail");
        loginProgress.dismiss();
        emailEt.setError(getString(R.string.empty_email));
    }

    @Override
    public void setPasswordError() {
        passwordEt.setErrorEnabled(true);
        Log.d(TAG, "errorPassword");
        loginProgress.dismiss();
        passwordEt.setError(getString(R.string.empty_password));
    }

    @Override
    public void navigateToHome() {
        Log.d(TAG, "navigate to home");
        loginProgress.dismiss();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "Login destroy");
        super.onDestroy();
    }

    @Override
    public void showToast(String message) {
        Log.d(TAG, "showToast");
        loginProgress.dismiss();
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
        Log.d(TAG, message);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    public void onLogIn(View view) {
        passwordEt.setErrorEnabled(false);
        emailEt.setErrorEnabled(false);
        Log.d(TAG, "OnLogin");
        loginProgress.setMessage(getString(R.string.wait_please));
        loginProgress.show();
        //  loginPresenter.login("ee@gmail.com", "132132132");
        loginPresenter.login(emailEt.getEditText().getText().toString(), passwordEt.getEditText().getText().toString());
    }

    public void onSingInActivityClicked(View view) {
        startActivity(new Intent(LoginActivity.this, SingUpActivity.class));
    }

    private void initUI() {
        emailEt = (TextInputLayout) findViewById(R.id.email_et);
        passwordEt = (TextInputLayout) findViewById(R.id.password_et);
        btnLogInFacebook = (LoginButton) findViewById(R.id.login_facebook_btn);
        loginProgress = new ProgressDialog(this);
        loginProgress.setCanceledOnTouchOutside(false);
        loginProgress.setCancelable(false);
    }

    public void onResetPassword(View view) {
        startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            Utils.hideKeyboard(getApplicationContext(), v);
            v.clearFocus();
        }
        if (hasFocus)
            Utils.showKeyboard(getApplicationContext(), v);
    }
}