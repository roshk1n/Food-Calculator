package com.example.roshk1n.foodcalculator.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.presenters.LoginPresenterImpl;
import com.example.roshk1n.foodcalculator.Views.LoginView;
import com.example.roshk1n.foodcalculator.remoteDB.FirebaseHelper;
import com.example.roshk1n.foodcalculator.utils.Utils;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends Activity implements LoginView {

    private final static String TAG = "MyLog";

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

        loginPresenter = new LoginPresenterImpl();
        loginPresenter.setView(this);

        loginPresenter.checkLogin();

        loginPresenter.loginFacebookListner(btnLogInFacebook);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(FirebaseHelper.getInstance().getmAuthListner() != null)
            FirebaseHelper.getInstance().removeListner();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseHelper.getInstance().addListner();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       loginPresenter.getCallbackManager().onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void setEmailError() {
        loginProgress.dismiss();
        emailEt.setError("Enter email please");
    }

    @Override
    public void setPasswordError() {
        loginProgress.dismiss();
        etPassword.setError("Enter password please");
    }

    @Override
    public void navigateToHome() {
        loginProgress.dismiss();
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();
        Log.d(TAG, "onAuthStateChanged:signed_in");
    }

    @Override
    public void showToast(String message) {
        loginProgress.dismiss();
        Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    public void onLogIn(View view) {
        loginProgress.setMessage("Login");
        loginProgress.setCanceledOnTouchOutside(false);
        loginProgress.show();
        loginPresenter.login(emailEt.getText().toString(),etPassword.getText().toString());
    }

    public void onSingInActivityClicked(View view) {
        startActivity(new Intent(getApplicationContext(), SingUpActivity.class));
    }

    private void initUI() {
        emailEt = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogInFacebook = (LoginButton) findViewById(R.id.btnLogInFacebook);
        loginProgress = new ProgressDialog(this);
    }
}