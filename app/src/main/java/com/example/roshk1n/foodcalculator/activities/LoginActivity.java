package com.example.roshk1n.foodcalculator.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.interfaces.OnCompleteCallback;
import com.example.roshk1n.foodcalculator.presenters.LoginPresenterImpl;
import com.example.roshk1n.foodcalculator.utils.Utils;
import com.example.roshk1n.foodcalculator.views.LoginView;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.Locale;

public class LoginActivity extends Activity implements LoginView, View.OnFocusChangeListener {
    private final static String TAG = LoginActivity.class.getSimpleName();
    private LoginPresenterImpl loginPresenter;
    private EditText emailEt;
    private EditText passwordEt;
    private LoginButton btnLogInFacebook;
    private ProgressDialog loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        initUI();

        emailEt.setOnFocusChangeListener(this);
        passwordEt.setOnFocusChangeListener(this);

        String languageToLoad  = "uk"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

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
        loginProgress.dismiss();
        emailEt.setError(getString(R.string.error_email));
    }

    @Override
    public void setPasswordError() {
        loginProgress.dismiss();
        passwordEt.setError(getString(R.string.error_password));
    }

    @Override
    public void navigateToHome() {
        loginProgress.dismiss();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void showToast(String message) {
        loginProgress.dismiss();
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    public void onLogIn(View view) {
        loginProgress.setMessage(getString(R.string.wait_please));
        loginProgress.show();
        loginPresenter.login("ee@gmail.com", "132132132");
    }

    public void onSingInActivityClicked(View view) {
        startActivity(new Intent(getApplicationContext(), SingUpActivity.class));
    }

    private void initUI() {
        emailEt = (EditText) findViewById(R.id.etLogin);
        passwordEt = (EditText) findViewById(R.id.etPassword);
        btnLogInFacebook = (LoginButton) findViewById(R.id.btnLogInFacebook);
        loginProgress = new ProgressDialog(this);
        loginProgress.setCanceledOnTouchOutside(false);
        loginProgress.setCancelable(false);
    }

    public void onResetPassword(View view) {
        startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus)
            Utils.hideKeyboard(getApplicationContext(),v);
    }
}