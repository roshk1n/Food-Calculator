package com.example.roshk1n.foodcalculator.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.presenters.LoginPresenterImpl;
import com.example.roshk1n.foodcalculator.views.LoginView;
import com.example.roshk1n.foodcalculator.remoteDB.FirebaseHelper;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

//TODO progress dialog
public class LoginActivity extends Activity implements LoginView {

    private final static String TAG = LoginActivity.class.getSimpleName();

    private LoginPresenterImpl loginPresenter;
    private EditText emailEt;
    private EditText etPassword;
    private LoginButton btnLogInFacebook;
    private ProgressDialog loginProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        initUI();

        Log.d("onCreate","onCreate");
        loginPresenter = new LoginPresenterImpl();
        loginPresenter.setView(this);

        loginPresenter.checkLogin();

        btnLogInFacebook.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        btnLogInFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper.getInstance().removeListener();
                loginPresenter.loginFacebookListner(btnLogInFacebook);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if(FirebaseHelper.getInstance().getAuthListener() != null)
            Log.d("LoginActivity", "remove OnStop listener");
            FirebaseHelper.getInstance().removeListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("LoginActivity", "OnStart add listener");
        FirebaseHelper.getInstance().addListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        loginProgress.setMessage("Wait please...");
        Log.d(TAG, "onActivityResult will show loader");
        loginProgress.show();
        loginPresenter.getCallbackManager().onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void setEmailError() {
        FirebaseHelper.getInstance().addListener();
        Log.d(TAG, "setEmailError will hide loader");
        loginProgress.dismiss();
        emailEt.setError("Enter email please");
    }

    @Override
    public void setPasswordError() {
        FirebaseHelper.getInstance().addListener();
        Log.d(TAG, "setPasswordError will hide loader");
        loginProgress.dismiss();
        etPassword.setError("Enter password please");
    }

    @Override
    public void navigateToHome() {
        Log.d(TAG, "navigateToHome will hide loader");
        loginProgress.dismiss();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
        Log.d(TAG, "onAuthStateChanged:signed_in");
    }

    @Override
    public void showToast(String message) {
        FirebaseHelper.getInstance().addListener();
        Log.d(TAG, "showToastLogin will hide loader");
        loginProgress.dismiss();
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void loginSuccessFacebook() {
        Log.d("LoginActivity", "facebook success add listener");
        FirebaseHelper.getInstance().addListener();
    }

    public void onLogIn(View view) {
        FirebaseHelper.getInstance().removeListener();
        loginProgress.setMessage("Wait please...");
        Log.d(TAG, "onLogIn will show loader");
        loginProgress.show();
//        loginPresenter.login(emailEt.getText().toString(), etPassword.getText().toString());
        loginPresenter.login("ee@gmail.com", "132132132");
    }

    public void onSingInActivityClicked(View view) {
        startActivity(new Intent(getApplicationContext(), SingUpActivity.class));
    }

    private void initUI() {
        emailEt = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogInFacebook = (LoginButton) findViewById(R.id.btnLogInFacebook);
        loginProgress = new ProgressDialog(this);
        loginProgress.setCanceledOnTouchOutside(false);
        loginProgress.setCancelable(false);
    }

    public void onResetPassword(View view) {
        startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
    }
}