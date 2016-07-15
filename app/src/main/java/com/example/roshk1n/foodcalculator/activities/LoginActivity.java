package com.example.roshk1n.foodcalculator.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.ManageLoginApi;
import com.example.roshk1n.foodcalculator.MyApplication;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.activities.presenters.LoginPresenterImpl;
import com.example.roshk1n.foodcalculator.activities.views.LoginView;
import com.example.roshk1n.foodcalculator.remoteDB.FirebaseHelper;
import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.login.widget.ProfilePictureView;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends Activity implements LoginView {

    private static String TAG = "MyLog";

    private Button btnLogIn;
    private TextView info;
    private EditText etEmail;
    private EditText etPassword;

    private Firebase firebase;

    //private LoginButton btnLogInFacebook;
    //private ProfilePictureView profilePictureView;
    //private CallbackManager callbackManager;

    private MyApplication myApplication;

    private LoginPresenterImpl loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();

        FacebookSdk.sdkInitialize(getApplicationContext());

        loginPresenter = new LoginPresenterImpl();
        loginPresenter.setView(this);
        loginPresenter.checkLogin();

        myApplication= (MyApplication) getApplicationContext();
        Log.d("My",myApplication.getCount()+"");

        // callbackManager = CallbackManager.Factory.create();

       //btnLogInFacebook = (LoginButton) findViewById(R.id.btnLogInFacebook);



       /* btnLogInFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() { //login via facebook
            @Override
            public void onSuccess(LoginResult loginResult) {
           info.setText(
                        "RegistrationUser ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n"
                );


                startActivity(new Intent(LoginActivity.this,MainActivity.class));

                // profilePictureView.setProfileId(loginResult.getAccessToken().getUserId());
            }
            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                info.setText("Login attempt failed.");
            }
        });*/

     //   profilePictureView = (ProfilePictureView) findViewById(R.id.ProfilePhotoFac);

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
       // callbackManager.onActivityResult(requestCode,resultCode,data);

    }

    @Override
    public void setEmailError() {
        etEmail.setError("Enter email please");
    }

    @Override
    public void setPasswordError() {
        etPassword.setError("Enter password please");
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        Log.d(TAG, "onAuthStateChanged:signed_in");
    }

    @Override
    public void failedAuth() {
        Toast.makeText(LoginActivity.this, "Authentication failed. Try again please!",Toast.LENGTH_SHORT).show();
    }

    public void onGoSingInActivityClicked(View view) {
        startActivity(new Intent(getApplicationContext(), SingUpActivity.class));
    }

    public void onLogIn(View view) { // кнопка логіну користувача через email/password
        loginPresenter.loginWithEmail(etEmail.getText().toString(),etPassword.getText().toString());
    }

    public void onLogInApi(View view) {
      //  loginPresenter.loginWithApi(etEmail.getText().toString(),etPassword.getText().toString()); невідомо шо там з апішкою )
    }

    private void initUI() {
        btnLogIn = (Button)  findViewById(R.id.btnLogin);
        etEmail = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        info = (TextView) findViewById(R.id.info);
    }
}
