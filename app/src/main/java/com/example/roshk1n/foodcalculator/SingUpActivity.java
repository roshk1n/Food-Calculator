package com.example.roshk1n.foodcalculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.login.LoginActivity;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingUpActivity extends Activity {

    private static String TAG = "MyLog";
    private static final int PICK_PHOTO_FOR_AVATAR = 0;
    private static final int MAKE_PHOTO = 1;

    private Firebase myFirebaseRef;
    private User user;
    private EditText surname;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private CircleImageView ivUser;

    private FirebaseAuth mAuth;
    private StorageReference storageRef;

    private byte[] dataImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        mAuth = FirebaseAuth.getInstance();
        MyApplication myApplication= (MyApplication) getApplicationContext();
        myApplication.setCount(myApplication.getCount()+1);
        Log.d("My",myApplication.getCount()+"");
        user = new User();
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
        ivUser = (CircleImageView) findViewById(R.id.ivUser);
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_PHOTO_FOR_AVATAR&& resultCode==Activity.RESULT_OK)
        {
            if(data==null)
            {
                return;
            }
            Bitmap mIcon;
            try {
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                 mIcon = BitmapFactory.decodeStream(inputStream);
                 //Log.d(TAG,data.getDataRegistration().getPath().toString());
                 ivUser.setImageBitmap(mIcon);

                Uri file = Uri.fromFile(new File(data.getData().getPath()));
                FirebaseStorage storage = FirebaseStorage.getInstance();
               storageRef = storage.getReferenceFromUrl("gs://food-calculator.appspot.com/");
               /* ivUser.setDrawingCacheEnabled(true);
                ivUser.buildDrawingCache();
                Bitmap bitmap = ivUser.getDrawingCache();
          */
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mIcon.compress(Bitmap.CompressFormat.JPEG,100,baos);
                dataImage =baos.toByteArray();
                StorageReference mountainsRef = storageRef.child("images/PHOTO"+file.getLastPathSegment());
                UploadTask uploadTask =mountainsRef.putBytes(dataImage);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.d(TAG,"Ok");
                        user.setPhotoUrl(downloadUrl.toString());
                    }
                });


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(requestCode==MAKE_PHOTO&& resultCode==Activity.RESULT_OK)
        {
            if(data==null)
            {
                return;
            }
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ivUser.setImageBitmap(photo);
        }
    }

    protected void setUpUser(){
        user.setFullname(surname.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
    }

    public void onChoosePhoto(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Photo")
                .setItems(R.array.photo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0) {
                            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            getIntent.setType("image/*");

                            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
    public void onSignUpClicked (View view) //реєстрація користувача в firebase
    {

        if(!surname.getText().toString().equals("")||!email.getText().toString().equals("")||!password.getText().toString().equals("")) {
            if(password.getText().toString().equals(confirmPassword.getText().toString())) {
                setUpUser();
                String url=null;
                if(user.getPhotoUrl()==null)
                    url = "https://firebasestorage.googleapis.com/v0/b/food-calculator.appspot.com/o/images%2Fprofle_default.png?alt=media&token=812c2e4f-45e0-4c41-bbaf-a5b94e1b95c7";
                else
                    url = user.getPhotoUrl();
                final String finalUrl = url;
                mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("MyLog", "createUserWithEmail:onComplete:"+ task.isSuccessful());

                                if (task.isSuccessful()) {
                                    final FirebaseUser dbuser = FirebaseAuth.getInstance().getCurrentUser();
                                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(user.getFullname().toString())
                                            .setPhotoUri(Uri.parse(finalUrl))
                                            .build();
                                    dbuser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Log.w(TAG, "RegistrationUser profile update");
                                                startActivity(new Intent(SingUpActivity.this,LoginActivity.class));
                                            }
                                        }
                                    });

                                }
                                else
                                {
                                    Toast.makeText(SingUpActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else
            {
                Toast.makeText(SingUpActivity.this, "Your passwords don`t match.",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else
        {

            Toast.makeText(SingUpActivity.this, "Enter all fields please.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onSignUpAPIClicked(View view)
    {

    }
}
