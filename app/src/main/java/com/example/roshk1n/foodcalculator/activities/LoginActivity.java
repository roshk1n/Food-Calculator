package com.example.roshk1n.foodcalculator.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.LinearLayout;

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
    private LinearLayout parentLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        loginProgress.setMessage(getString(R.string.wait_please));
        loginProgress.show();
        loginPresenter.getCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setEmailError() {
        emailEt.setErrorEnabled(true);
        loginProgress.dismiss();
        emailEt.setError(getString(R.string.empty_email));
    }

    @Override
    public void setPasswordError() {
        passwordEt.setErrorEnabled(true);
        loginProgress.dismiss();
        passwordEt.setError(getString(R.string.empty_password));
    }

    @Override
    public void navigateToHome() {
        loginProgress.dismiss();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        loginPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public void showMessage(String message) {
        loginProgress.dismiss();
        Snackbar.make(parentLinear, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    public void onLogIn(View view) {
        passwordEt.setErrorEnabled(false);
        emailEt.setErrorEnabled(false);
        loginProgress.setMessage(getString(R.string.wait_please));
        loginProgress.show();
        loginPresenter.login(emailEt.getEditText().getText().toString(), passwordEt.getEditText().getText().toString());
    }

    public void onSingInActivityClicked(View view) {
        startActivity(new Intent(LoginActivity.this, SingUpActivity.class));
    }

    private void initUI() {
        emailEt = (TextInputLayout) findViewById(R.id.email_et);
        passwordEt = (TextInputLayout) findViewById(R.id.password_et);
        btnLogInFacebook = (LoginButton) findViewById(R.id.login_facebook_btn);
        parentLinear = (LinearLayout) findViewById(R.id.parent_login_layout);
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