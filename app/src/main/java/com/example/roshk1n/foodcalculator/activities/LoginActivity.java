package com.example.roshk1n.foodcalculator.activities;

import android.app.Activity;
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
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends Activity implements LoginView {

    private final static String TAG = "MyLog";

    private LoginPresenterImpl loginPresenter;
    private Button loginBtn;
    private EditText emailEt;
    private EditText etPassword;
    private LoginButton btnLogInFacebook;

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

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.login(emailEt.getText().toString(),etPassword.getText().toString());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if(FirebaseHelper.getmAuthListner() != null) {
            FirebaseHelper.removeListner();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseHelper.addListner();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       loginPresenter.getCallbackManager().onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void setEmailError() {
        emailEt.setError("Enter email please");
    }

    @Override
    public void setPasswordError() {
        etPassword.setError("Enter password please");
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();
        Log.d(TAG, "onAuthStateChanged:signed_in");
    }
    @Override
    public void showToast(String message) {
        Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    public void onLogIn(View view) {
        loginPresenter.login(emailEt.getText().toString(),etPassword.getText().toString());
    }
    public void onGoSingInActivityClicked(View view) {
        startActivity(new Intent(getApplicationContext(), SingUpActivity.class));
    }

    private void initUI() {
        emailEt = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogInFacebook = (LoginButton) findViewById(R.id.btnLogInFacebook);
        loginBtn = (Button) findViewById(R.id.login_btn);
    }
}