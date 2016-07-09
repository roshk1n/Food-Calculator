package com.example.roshk1n.foodcalculator;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Map;


public class LoginActivity extends Activity {

    private static String TAG = "MyLog";
    Firebase firebase;
    LoginButton btnLogInFacebook;
    Button btnLogIn;
    TextView info;
    EditText etLogin;
    EditText etPassword;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;


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

        mAuth = FirebaseAuth.getInstance(); // перевірка статусу логіну
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user !=null){
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
                else
                {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        btnLogInFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() { //login via facebook
            @Override
            public void onSuccess(LoginResult loginResult) {
  /*              info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n"
                );*/

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
        });

     //   profilePictureView = (ProfilePictureView) findViewById(R.id.ProfilePhotoFac);


       /* Firebase.setAndroidContext(this);
        firebase = new Firebase("https://food-calculator.firebaseio.com/");
        firebase.child("condition").setValue("Do you have data? You'll love Firebase.");*/

    }

    public void onStop()
    {
        super.onStop();
        if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner);
        }
    }
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode,resultCode,data);

    }
    public void onGoSingInActivityClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), SingUpActivity.class);
        startActivity(intent); //перехід на SingUpActivity
    }
    public void onLogIn(View view) // кнопка логіну користувача через email/password
    {
        if(etLogin.getText().toString().equals("")||etPassword.getText().toString().equals(""))
        {
            Toast.makeText(LoginActivity.this, "Enter all the fields.",Toast.LENGTH_SHORT).show();
        }
        else
        {
        mAuth.signInWithEmailAndPassword(etLogin.getText().toString(),etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed. Try again please!",Toast.LENGTH_SHORT).show();
                        }
                    }

                });
        }


    }

    public void  onLogInApi(View view)
    {
        User user = new User("11","Vova","vova@gmail.com","132132132","https://firebasestorage.googleapis.com/v0/b/food-calculator.appspot.com/o/images%2Fprofle_default.png?alt=media&token=812c2e4f-45e0-4c41-bbaf-a5b94e1b95c7");
        user.saveUser();
       // Firebase ref = new Firebase("https://food-calculator.firebaseio.com/users/");

    }

}
