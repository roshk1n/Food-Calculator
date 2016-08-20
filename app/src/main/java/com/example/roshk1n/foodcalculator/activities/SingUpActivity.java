package com.example.roshk1n.foodcalculator.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.presenters.SingUpPresenterImpl;
import com.example.roshk1n.foodcalculator.Views.SingUpView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingUpActivity extends Activity implements SingUpView {

    private static final int PICK_PHOTO_FOR_AVATAR = 0;
    private static final int MAKE_PHOTO = 1;
    private static final String TAG = "MyLog";

    private EditText surname;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private CircleImageView ivUser;

    private SingUpPresenterImpl singUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        initUI();

        singUpPresenter = new SingUpPresenterImpl();
        singUpPresenter.setView(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_PHOTO_FOR_AVATAR&& resultCode==Activity.RESULT_OK) {
           singUpPresenter.setUserPhotoSD(data,getApplicationContext().getContentResolver());
        }
        if(requestCode==MAKE_PHOTO && resultCode==Activity.RESULT_OK) {
            singUpPresenter.setUserPhotoCamera(data);
        }
    }

    @Override
    public void setUserPhoto(Bitmap bitmap) {
        ivUser.setImageBitmap(bitmap);
    }

    @Override
    public Bitmap getBitmapIv() {
        ivUser.setDrawingCacheEnabled(true);
        ivUser.buildDrawingCache();
        return ivUser.getDrawingCache();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(SingUpActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToLogin() {
        startActivity(new Intent(SingUpActivity.this,LoginActivity.class));
        finish();
        Log.d(TAG, "onAuthStateChanged:signed_in");
    }

    public void onSignUpClicked (View view) {
        singUpPresenter.singUp(surname.getText().toString(),email.getText().toString()
                ,password.getText().toString(),confirmPassword.getText().toString());
    }

    private void initUI() {
        surname = (EditText) findViewById(R.id.edit_text_username);
        email = (EditText) findViewById(R.id.edit_text_new_email);
        password = (EditText) findViewById(R.id.edit_text_new_password);
        confirmPassword = (EditText) findViewById(R.id.edit_text_confirm_password);
        ivUser = (CircleImageView) findViewById(R.id.ivUser);
    }

    public void onChoosePhoto(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Photo")
                .setItems(R.array.photo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0) {
                            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            getIntent.setType("image/*");

                            Intent pickIntent = new Intent(Intent.ACTION_PICK
                                    , android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            pickIntent.setType("image/*");

                            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                            startActivityForResult(chooserIntent, PICK_PHOTO_FOR_AVATAR);
                        }
                        else
                        {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, MAKE_PHOTO);
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
