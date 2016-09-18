package com.example.roshk1n.foodcalculator.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.presenters.SingUpPresenterImpl;
import com.example.roshk1n.foodcalculator.utils.Utils;
import com.example.roshk1n.foodcalculator.views.SingUpView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingUpActivity extends Activity implements SingUpView, View.OnFocusChangeListener {

    private static final int PICK_PHOTO_FOR_AVATAR = 0;
    private static final int MAKE_PHOTO = 1;
    private static final String TAG = SingUpActivity.class.getSimpleName();
    private SingUpPresenterImpl singUpPresenter;

    private TextInputLayout surnameEt;
    private TextInputLayout emailEt;
    private TextInputLayout passwordEt;
    private TextInputLayout confirmPasswordEt;
    private TextView alreadyAccountTv;
    private CircleImageView userIv;
    private ProgressDialog singUpProgress;
    private RelativeLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "SingUpActivity");

        setContentView(R.layout.activity_sing_up);
        initUI();

        surnameEt.setOnFocusChangeListener(this);
        emailEt.setOnFocusChangeListener(this);
        passwordEt.setOnFocusChangeListener(this);
        confirmPasswordEt.setOnFocusChangeListener(this);

        singUpPresenter = new SingUpPresenterImpl();
        singUpPresenter.setView(this);

        alreadyAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            singUpPresenter.setUserPhotoSD(data, getApplicationContext().getContentResolver());
        }
        if (requestCode == MAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            singUpPresenter.setUserPhotoCamera(data);
        }
    }

    @Override
    protected void onDestroy() {
        singUpPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public void setUserPhoto(Bitmap bitmap) {
        userIv.setImageBitmap(bitmap);
    }

    @Override
    public Bitmap getBitmapIv() {
        userIv.setDrawingCacheEnabled(true);
        userIv.buildDrawingCache();
        return userIv.getDrawingCache();
    }

    @Override
    public void showSnackBar(String message) {
        singUpProgress.dismiss();
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToLogin() {
        singUpProgress.dismiss();
        startActivity(new Intent(SingUpActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setEmailError(String message) {
        singUpProgress.dismiss();
        emailEt.setErrorEnabled(true);
        emailEt.setError(message);
    }

    @Override
    public void setPasswordError(String message) {
        singUpProgress.dismiss();
        passwordEt.setErrorEnabled(true);
        passwordEt.setError(message);
    }

    @Override
    public void setConfirmError(String message) {
        singUpProgress.dismiss();
        confirmPasswordEt.setErrorEnabled(true);
        confirmPasswordEt.setError(message);
    }

    @Override
    public void serFullNameError(String message) {
        singUpProgress.dismiss();
        surnameEt.setErrorEnabled(true);
        surnameEt.setError(message);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus) {
            Utils.hideKeyboard(getApplicationContext(),v);
            v.clearFocus();
        }
        if(hasFocus)
            Utils.showKeyboard(getApplicationContext(),v);
    }

    public void onSignUpClicked(View view) {
        surnameEt.setErrorEnabled(false);
        emailEt.setErrorEnabled(false);
        passwordEt.setErrorEnabled(false);
        confirmPasswordEt.setErrorEnabled(false);

        singUpProgress = ProgressDialog.show(this, "", getString(R.string.wait_please));
        singUpProgress.setCanceledOnTouchOutside(false);
        singUpProgress.setCancelable(false);
        singUpPresenter.singUp(surnameEt.getEditText().getText().toString(), emailEt.getEditText().getText().toString()
                , passwordEt.getEditText().getText().toString(), confirmPasswordEt.getEditText().getText().toString());
    }

    private void initUI() {
        surnameEt = (TextInputLayout) findViewById(R.id.username_et);
        emailEt = (TextInputLayout) findViewById(R.id.new_email_et);
        passwordEt = (TextInputLayout) findViewById(R.id.password_sing_up_et);
        confirmPasswordEt = (TextInputLayout) findViewById(R.id.confirm_password_et);
        userIv = (CircleImageView) findViewById(R.id.ivUser);
        alreadyAccountTv = (TextView) findViewById(R.id.already_account_tv);
        parentLayout = (RelativeLayout) findViewById(R.id.parent_sing_up_layout);
    }

    public void onChoosePhoto(View view) {
        singUpPresenter.getPermissions();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.photo)
                .setItems(R.array.photo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            getIntent.setType("image/*");

                            Intent pickIntent = new Intent(Intent.ACTION_PICK
                                    , android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            pickIntent.setType("image/*");

                            Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.select_image));
                            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                            startActivityForResult(chooserIntent, PICK_PHOTO_FOR_AVATAR);
                        } else {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, MAKE_PHOTO);
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
