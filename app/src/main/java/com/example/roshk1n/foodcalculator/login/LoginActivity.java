package com.example.roshk1n.foodcalculator.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.MyApplication;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.main.MainActivity;
import com.example.roshk1n.foodcalculator.remoteDB.FirebaseHelper;
import com.example.roshk1n.foodcalculator.singup.SingUpActivity;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends Activity implements LoginView {

    private static String TAG = "MyLog";

    private LoginPresenterImpl loginPresenter;
    private Button loginBtn;
    private EditText emailEt;
    private EditText etPassword;
    private LoginButton btnLogInFacebook;

    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        initUI();

        loginPresenter = new LoginPresenterImpl();
        loginPresenter.setView(this);
        loginPresenter.checkLogin();

//        myApplication= (MyApplication) getApplicationContext();
//        Log.d("My",myApplication.getCount()+"");

        loginPresenter.loginFacebookListner(btnLogInFacebook);
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
        Log.d(TAG, "onAuthStateChanged:signed_in");
    }
    @Override
    public void showToast(String message) {
        Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    public void onGoSingInActivityClicked(View view) {
        startActivity(new Intent(getApplicationContext(), SingUpActivity.class));
    }

    public void onLogIn(View view) { // кнопка логіну користувача через email/password
        loginPresenter.loginWithEmail(emailEt.getText().toString(),etPassword.getText().toString());
    }

    public void onLogInApi(View view) {
      //  loginPresenter.loginWithApi(emailEt.getText().toString(),etPassword.getText().toString()); невідомо шо там з апішкою )
    }

    private void initUI() {
        emailEt = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        loginBtn = (Button) findViewById(R.id.btnLogin);
        btnLogInFacebook = (LoginButton) findViewById(R.id.btnLogInFacebook);
    }
}
