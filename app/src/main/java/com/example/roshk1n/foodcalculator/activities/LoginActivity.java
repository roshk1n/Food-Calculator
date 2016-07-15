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
    private Firebase firebase;
   // private LoginButton btnLogInFacebook;
    private Button btnLogIn;
    private TextView info;
    private EditText etEmail;
    private EditText etPassword;
    //private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;

    private LoginPresenterImpl loginPresenter;


    ProfilePictureView profilePictureView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        loginPresenter = new LoginPresenterImpl();
        loginPresenter.setView(this);

        MyApplication myApplication= (MyApplication) getApplicationContext();
        Log.d("My",myApplication.getCount()+"");


        // callbackManager = CallbackManager.Factory.create();

      //btnLogInFacebook = (LoginButton) findViewById(R.id.btnLogInFacebook);
        btnLogIn = (Button)  findViewById(R.id.btnLogin);
        etEmail = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        info = (TextView) findViewById(R.id.info);
        //mAuth = FirebaseAuth.getInstance(); // перевірка статусу логіну

/*
        FirebaseHelper.setmAuth(FirebaseAuth.getInstance());
        FirebaseHelper.setmAuthListner(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));

                } else {

                }
            }
        });
*/

        loginPresenter.checkLogin();
/*

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
*/

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


       /* Firebase.setAndroidContext(this);
        firebase = new Firebase("https://food-calculator.firebaseio.com/");
        firebase.child("condition").setValue("Do you have data? You'll love Firebase.");*/

    }

    public void onStop()
    {
        super.onStop();
  /*      if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner);
            }*/

        if(FirebaseHelper.getmAuthListner() != null) {
            FirebaseHelper.removeListner();
        }


    }
    public void onStart()
    {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListner);
        FirebaseHelper.addListner();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
       // callbackManager.onActivityResult(requestCode,resultCode,data);

    }
    public void onGoSingInActivityClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), SingUpActivity.class);
        startActivity(intent); //перехід на SingUpActivity
    }
    public void onLogIn(View view) // кнопка логіну користувача через email/password
    {

        loginPresenter.loginWithEmail(etEmail.getText().toString(),etPassword.getText().toString());
        /*mAuth.signInWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed. Try again please!",Toast.LENGTH_SHORT).show();
                        }
                    }

               });*/

    }




/*    private class HttpAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            //return ManageLoginAPI.registerUser("Test","Test","roshk1n.ua@gmail.com","132132132","0"); /реєстрація
           // return ManageLoginAPI.verifyUser("roshk1n.ua@gmail.com"); /Перевірка
            return ManageLoginAPI.activationUser("c048a413fd1766ca252c1c01338e7b692dd5ad37","roshk1n.ua@gmail.com"); //активація
          //  return ManageLoginAPI.login("roshk1n.ua@gmail.com","132132132"); //login
          //  return ManageLoginAPI.logout("roshk1n.ua@gmail.com",""); //logout
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "DataRegistration Sent!" + result, Toast.LENGTH_LONG).show();
            Log.d(TAG,result);
        }
    } */
    public void  onLogInApi(View view) {
       /* new HttpAsyncTask().execute();

    *//**//*    RegistrationUser user = new RegistrationUser("11","Vova","vova@gmail.com","132132132","https://firebasestorage.googleapis.com/v0/b/food-calculator.appspot.com/o/images%2Fprofle_default.png?alt=media&token=812c2e4f-45e0-4c41-bbaf-a5b94e1b95c7");
        user.saveUser();*//**//*
       // Firebase ref = new Firebase("https://food-calculator.firebaseio.com/users/");*/

           //ManageLoginApi.registerUser("Oleh", "Roshka", "roshk1n.ua@gmail.com", "132132132");
        loginPresenter.login(etEmail.getText().toString(),etPassword.getText().toString());
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
    }

    @Override
    public void failedAuth() {

    }
}
