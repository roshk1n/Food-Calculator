package com.example.roshk1n.foodcalculator.remoteDB;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.User;
import com.example.roshk1n.foodcalculator.login.LoginActivity;
import com.example.roshk1n.foodcalculator.singup.ResponseListentenerUpload;
import com.example.roshk1n.foodcalculator.singup.SingUpPresenter;
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

/**
 * Created by roshk1n on 7/14/2016.
 */
public class FirebaseHelper {
    private static FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthListner;
    private static Firebase mFirebaseRef;
    private static FirebaseUser mFirebaseUser;
    private static FirebaseStorage storage = FirebaseStorage.getInstance();

//    private static String urlUserPhto;

    public static FirebaseStorage getStorage() { return storage; }

    public static void setStorage(FirebaseStorage storage) { FirebaseHelper.storage = storage; }

    public static FirebaseAuth getmAuth() {
        return mAuth;
    }

    public static void setmAuth(FirebaseAuth mAuth) {
        FirebaseHelper.mAuth = mAuth;
    }

    public static FirebaseAuth.AuthStateListener getmAuthListner() {
        return mAuthListner;
    }

    public static void setmAuthListner(FirebaseAuth.AuthStateListener mAuthListner) {
        FirebaseHelper.mAuthListner = mAuthListner;
    }

  /*  public static String getUrlUserPhto() { return urlUserPhto; }

    public static void setUrlUserPhto(String urlUserPhto) {
        FirebaseHelper.urlUserPhto = urlUserPhto;
    }*/

    public static FirebaseUser getmFirebaseUser() {
        return mFirebaseUser;
    }

    public static void setmFirebaseUser(FirebaseUser mFirebaseUser) {
        FirebaseHelper.mFirebaseUser = mFirebaseUser;
    }

    public static void removeListner() {
        mAuth.removeAuthStateListener(mAuthListner);
    }

    public static void addListner() {
        mAuth.addAuthStateListener(mAuthListner);
    }

    public static void logInWhithEmail(String email, String password, OnCompleteListener listner) {

         mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(listner);
    }

    public static void createUser(final User user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("MyLog", "createUserWithEmail:onComplete:"+ task.isSuccessful());

                        if (task.isSuccessful()) {
                            mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(user.getFullname())
                                    .setPhotoUri(Uri.parse(user.getPhotoUrl()))
                                    .build();
                            mFirebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });
                        }
                    }
                });
    }

    public static void uploadImage(Bitmap image, String fileName, final ResponseListentenerUpload upload) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,baos);
        StorageReference storage = FirebaseHelper
                .getStorage()
                .getReferenceFromUrl("gs://food-calculator.appspot.com/");

        StorageReference mountainsRef = storage
                .child("images/profile/"+fileName+"_profile_photo");

        UploadTask uploadTask =mountainsRef.putBytes(baos.toByteArray());
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                upload.onSuccess(downloadUrl.toString());
                /*urlUserPhto =  downloadUrl.toString();*/
            }
        });


    }
}

