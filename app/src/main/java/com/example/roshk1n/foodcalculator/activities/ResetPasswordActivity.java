package com.example.roshk1n.foodcalculator.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.presenters.ResetPasswordPresenterImpl;
import com.example.roshk1n.foodcalculator.views.ResetPasswordView;

public class ResetPasswordActivity extends AppCompatActivity implements ResetPasswordView {
    private Button resetPasswordBtn;
    private EditText emailEt;
    private TextView backLoginTv;
    private ResetPasswordPresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initUI();

        presenter = new ResetPasswordPresenterImpl();
        presenter.setView(this);

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.resetPassword(emailEt.getText().toString());
            }
        });
        backLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void errorEmail() {
        emailEt.setError("Your email is incorrect.");
    }

    private void initUI() {
        getSupportActionBar().hide();
        resetPasswordBtn = (Button) findViewById(R.id.reset_password_btn);
        emailEt = (EditText) findViewById(R.id.email_reset_password_et);
        backLoginTv = (TextView) findViewById(R.id.back_login_tv);
    }
}
