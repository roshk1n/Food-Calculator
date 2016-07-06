package com.example.roshk1n.foodcalculator;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;


public class LoginActivity extends Activity {
    Firebase firebase;
    LoginButton btnLogInFacebook;
    Button btnLogIn;
    TextView info;
    EditText etLogin;
    EditText etPassword;
    private CallbackManager callbackManager;
    ProfilePictureView profilePictureView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        btnLogInFacebook = (LoginButton) findViewById(R.id.btnLogInFacebook);
        btnLogIn = (Button)  findViewById(R.id.btnLogin);
        etLogin = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        info = (TextView) findViewById(R.id.info);

     //   profilePictureView = (ProfilePictureView) findViewById(R.id.ProfilePhotoFac);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Firebase refFire = new Firebase("https://food-calculator.firebaseio.com/");
              //  firebase.child("condition").setValue("Do you have data? You'll love Firebase.");
               // Firebase ref = new Firebase("https://<YOUR-FIREBASE-APP>.firebaseio.com");

                refFire.authWithPassword("roshka@gmail.com", "132132132", new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        info.setText(authData.getUid());
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        info.setText(firebaseError.getMessage());
                    }
                });
            }
        });
        btnLogInFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() { //login via facebook
            @Override
            public void onSuccess(LoginResult loginResult) {
                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n"
                );


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
        });







       /* Firebase.setAndroidContext(this);
        firebase = new Firebase("https://food-calculator.firebaseio.com/");
        firebase.child("condition").setValue("Do you have data? You'll love Firebase.");*/

    }
    protected void onStart()
    {
        super.onStart();


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode,resultCode,data);

    }
    public void onGoSingInActivityClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), SingUpActivity.class);
        startActivity(intent); //перехід на SingUpActivity
    }

}
