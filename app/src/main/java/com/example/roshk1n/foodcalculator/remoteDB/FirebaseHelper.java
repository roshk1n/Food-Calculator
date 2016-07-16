package com.example.roshk1n.foodcalculator.remoteDB;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by roshk1n on 7/14/2016.
 */
public class FirebaseHelper {
    private static FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthListner;

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

    public static void removeListner() {
        mAuth.removeAuthStateListener(mAuthListner);
    }

    public static void addListner() {
        mAuth.addAuthStateListener(mAuthListner);
    }

    public static void logInWhithEmail(String email, String password, OnCompleteListener listner) {
         mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(listner);
    }
}
