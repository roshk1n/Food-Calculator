package com.example.roshk1n.foodcalculator;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class SingUpActivity extends Activity {

    private Firebase myFirebaseRef;
    private User user;
    private EditText surname;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        mAuth = FirebaseAuth.getInstance();

        myFirebaseRef = new Firebase("https://food-calculator.firebaseio.com/");
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        surname = (EditText) findViewById(R.id.edit_text_username);
        email = (EditText) findViewById(R.id.edit_text_new_email);
        password = (EditText) findViewById(R.id.edit_text_new_password);
        confirmPassword = (EditText) findViewById(R.id.edit_text_confirm_password);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_sign_up);

    }
    @Override
    public void onStop() {
        super.onStop();

    }

    protected void setUpUser(){
        user = new User();
        user.setName(surname.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());

    }

    public void onSignUpClicked (View view) //реєстрація користувача в firebase
    {
        if(password.getText().toString().equals(confirmPassword.getText().toString())) {
            if(surname.getText().toString()!=("")||email.getText().toString()!=""||password.getText().toString()!="") {
                setUpUser();
                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("MyLog", "createUserWithEmail:onComplete:"+ task.isSuccessful());

                                if (!task.isSuccessful()) {

                                        Toast.makeText(SingUpActivity.this, task.getException().getMessage().toString(),
                                                Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    startActivity(new Intent(SingUpActivity.this,MainActivity.class));
                                }
                            }
                        });
            }
            else
            {
                Toast.makeText(SingUpActivity.this, "Enter all fields please.",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(SingUpActivity.this, "Your passwords don`t match.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
