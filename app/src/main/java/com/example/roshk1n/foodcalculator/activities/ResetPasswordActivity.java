package com.example.roshk1n.foodcalculator.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.presenters.ResetPasswordPresenterImpl;
import com.example.roshk1n.foodcalculator.utils.Utils;
import com.example.roshk1n.foodcalculator.views.ResetPasswordView;

public class ResetPasswordActivity extends AppCompatActivity implements ResetPasswordView, View.OnFocusChangeListener {
    private Button resetPasswordBtn;
    private EditText emailEt;
    private TextView backLoginTv;
    private ResetPasswordPresenterImpl presenter;
    private LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initUI();

        presenter = new ResetPasswordPresenterImpl();
        presenter.setView(this);

        emailEt.setOnFocusChangeListener(this);

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
    protected void onDestroy() {
        presenter.destroy();
        super.onDestroy();
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void errorEmail() {
        emailEt.setError(getString(R.string.email_incorrect));
    }

    private void initUI() {
        getSupportActionBar().hide();
        parentLayout = (LinearLayout) findViewById(R.id.parent_reset_layout);
        resetPasswordBtn = (Button) findViewById(R.id.reset_password_btn);
        emailEt = (EditText) findViewById(R.id.email_reset_et);
        backLoginTv = (TextView) findViewById(R.id.back_login_tv);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus)
            Utils.hideKeyboard(getApplicationContext(), v);
    }
}
