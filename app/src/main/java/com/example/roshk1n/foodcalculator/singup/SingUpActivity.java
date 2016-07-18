package com.example.roshk1n.foodcalculator.singup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.main.MainActivity;
import com.example.roshk1n.foodcalculator.R;


import de.hdodenhof.circleimageview.CircleImageView;

public class SingUpActivity extends Activity implements SingUpView {

    private static final int PICK_PHOTO_FOR_AVATAR = 0;
    private static final int MAKE_PHOTO = 1;
    private static String TAG="MyLog";

    private EditText surname;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private CircleImageView ivUser;

    private SingUpPresenterImpl singUpPresente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        initUI();

        singUpPresente = new SingUpPresenterImpl();
        singUpPresente.setView(this);
    }
    @Override
    protected void onStart()
    {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_PHOTO_FOR_AVATAR&& resultCode==Activity.RESULT_OK) {
           singUpPresente.setUserPhotoSD(data,getApplicationContext().getContentResolver());
        }
        if(requestCode==MAKE_PHOTO&& resultCode==Activity.RESULT_OK) {
            singUpPresente.setUserPhotoCamera(data);
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
        Bitmap bitmap = ivUser.getDrawingCache();
        return bitmap;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(SingUpActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(SingUpActivity.this,MainActivity.class));
        Log.d(TAG, "onAuthStateChanged:signed_in");
    }

    public void onSignUpClicked (View view) { //реєстрація користувача в firebase
        singUpPresente.singUpFirebase(surname.getText().toString(),email.getText().toString()
                ,password.getText().toString(),confirmPassword.getText().toString());
    }

    private void initUI() {
        surname = (EditText) findViewById(R.id.edit_text_username);
        email = (EditText) findViewById(R.id.edit_text_new_email);
        password = (EditText) findViewById(R.id.edit_text_new_password);
        confirmPassword = (EditText) findViewById(R.id.edit_text_confirm_password);
        ivUser = (CircleImageView) findViewById(R.id.ivUser);
    }

    public void onSignUpAPIClicked(View view) {}

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
