package com.example.roshk1n.foodcalculator.remoteDB;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.User;
import com.example.roshk1n.foodcalculator.interfaces.FirebaseCallback;
import com.example.roshk1n.foodcalculator.interfaces.LoadDayCallback;
import com.example.roshk1n.foodcalculator.interfaces.LoadDayFirebaseCallback;
import com.example.roshk1n.foodcalculator.remoteDB.model.DayFirebase;
import com.example.roshk1n.foodcalculator.remoteDB.model.FoodFirebase;
import com.example.roshk1n.foodcalculator.remoteDB.model.UserFirebase;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.interfaces.ResponseListentenerUpload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FirebaseHelper {
    private static Date date = new Date();
    private DayFirebase dayFirebase = new DayFirebase();
    private static List<FoodFirebase> foodsUpdate = new ArrayList<>();
    private static FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUser mFirebaseUser;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private FirebaseCallback firebaseCallback;

    public FirebaseHelper(FirebaseCallback callback) {
        this.firebaseCallback = callback;
    }


    public  FirebaseStorage getStorage() {
        return storage;
    }

    public  void setStorage(FirebaseStorage storage) {
        this.storage = storage;
    }

    public  FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public static FirebaseAuth.AuthStateListener getmAuthListner() {
        return mAuthListner;
    }

    public void setmAuthListner(FirebaseAuth.AuthStateListener mAuthListner) {
        this.mAuthListner = mAuthListner;
    }

    public FirebaseUser getmFirebaseUser() {
        return mFirebaseUser;
    }

    public void setmFirebaseUser(FirebaseUser mFirebaseUser) {
        this.mFirebaseUser = mFirebaseUser;
    }

    public static void removeListner() {
        mAuth.removeAuthStateListener(mAuthListner);
    }

    public static void addListner() {
        mAuth.addAuthStateListener(mAuthListner);
    }

    public void logInWithEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    setmFirebaseUser(getmFirebaseUser());
                    setmFirebaseUser(getmAuth().getCurrentUser());
                    Session.startSession();
                    Session.getInstance().setEmail(getmFirebaseUser().getEmail());
                    Session.getInstance().setFullname(getmFirebaseUser().getDisplayName());
                    Session.getInstance().setUrlPhoto(String.valueOf(getmFirebaseUser().getPhotoUrl()));
                    firebaseCallback.loginSuccessful();
                } else {
                    firebaseCallback.showToast("Authentication failed. Try again please!");
                }
            }
        });
    }

    public void createUser(final User user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("MyLog", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(user.getFullname())
                                    .setPhotoUri(Uri.parse(user.getPhotoUrl()))
                                    .build();
                            mFirebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        UserFirebase user = new UserFirebase();
                                        user.setAge(20l);
                                        user.setActiveLevel("none");
                                        user.setHeight(175l);
                                        user.setSex("none");
                                        user.setWeight(70l);
                                        user.setGoalCalories(0l);
                                        DatabaseReference reference = database.getReference().child("users").child(getmAuth().getCurrentUser().getUid());
                                        reference.setValue(user);
                                    }
                                }
                            });
                        }
                    }
                });
    }

    public void uploadImage(Bitmap image, String fileName, final ResponseListentenerUpload upload) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        StorageReference storage = getStorage()
                .getReferenceFromUrl("gs://food-calculator.appspot.com/");

        StorageReference mountainsRef = storage
                .child("images/profile/" + fileName + "_profile_photo");

        UploadTask uploadTask = mountainsRef.putBytes(baos.toByteArray());
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
            }
        });
    }

    public void addFood(Food food) {
        Date date3 = new Date(food.getTime());
        dayFirebase = new DayFirebase();
        if (date.getDate() == date3.getDate()
                && date.getMonth() == date3.getMonth()
                && date.getYear() == date3.getYear()) {
            dayFirebase.setFoods(foodsUpdate);
            dayFirebase.getFoods().add(new FoodFirebase(food));
        } else {
            dayFirebase.setFoods(foodsUpdate);
            dayFirebase.getFoods().add(new FoodFirebase(food));
            date = date3;
        }

        DatabaseReference reference = database.getReference("users");
        DatabaseReference userRef = reference.child(getmAuth().getCurrentUser().getUid()).child("days").child(date3.getDate() + "_" + date3.getMonth() + "_" + date3.getYear());
/*
        DatabaseReference dayRef = userRef.child("days").child(date.getDate()+"_"+date.getMonth()+"_"+date.getYear());
        DatabaseReference foodRef = dayRef.child("foods").child(food.getTime()+"");
        DatabaseReference nutrientsRef = foodRef.child("nutrients");
*/
        dayFirebase.setDate(food.getTime());
        userRef.setValue(dayFirebase);

   /*     foodRef.child("ndbno").setValue(food.getNdbno());
        foodRef.child("name").setValue(food.getName());
        foodRef.child("portion").setValue(food.getPortion());
        foodRef.child("time").setValue(food.getTime());

        for (int i = 0; i < food.getNutrients().size(); i++) {
            DatabaseReference nut = nutrientsRef.child(food.getNutrients().get(i).getNutrient_id());
            nut.child("nutrient_id").setValue(food.getNutrients().get(i).getNutrient_id());
            nut.child("name").setValue(food.getNutrients().get(i).getName());
            nut.child("unit").setValue(food.getNutrients().get(i).getUnit());
            nut.child("value").setValue(food.getNutrients().get(i).getValue());
        } */
    }

    public void removeFood(int position) {
        DatabaseReference reference = database.getReference("users");
        Date date = new Date(dayFirebase.getDate());
        DatabaseReference userRef = reference.child(getmAuth().getCurrentUser().getUid()).child("days").child(date.getDate() + "_" + date.getMonth() + "_" + date.getYear());
        dayFirebase.getFoods().remove(position);
        foodsUpdate.remove(position);
        userRef.setValue(dayFirebase);
    }

    public void loadDay(final Date date, final LoadDayCallback loadDayCallback) {
        DatabaseReference reference = database.getReference("users");
        final DatabaseReference dayRef = reference
                .child(getmAuth().getCurrentUser().getUid())
                .child("days")
                .child(date.getDate() + "_" + date.getMonth() + "_" + date.getYear());
        DatabaseReference foodRef = dayRef.child("foods");
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Count ", "" + dataSnapshot.getChildrenCount());
                final Day day = new Day();
                ArrayList<Food> foods = new ArrayList<Food>();
                foodsUpdate.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    foodsUpdate.add(postSnapshot.getValue(FoodFirebase.class));
                    foods.add(new Food(postSnapshot.getValue(FoodFirebase.class)));
                    Log.e("Get Data", foodsUpdate.get(0).getName() + "");
                }
                day.setFoods(foods);
                dayRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("date").getValue() != null) {
                            day.setDate((long) dataSnapshot.child("date").getValue());
                            dayFirebase = new DayFirebase(day);
                            loadDayCallback.loadComplete(day);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void checkLogin() {
        setmAuth(FirebaseAuth.getInstance());
        setmAuthListner(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setmFirebaseUser(getmAuth().getCurrentUser());
                    Session.startSession();
                    Session.getInstance().setEmail(user.getEmail());
                    Session.getInstance().setFullname(user.getDisplayName());
                    Session.getInstance().setUrlPhoto(String.valueOf(user.getPhotoUrl()));
                    firebaseCallback.loginSuccessful();
                }
            }
        });
    }
}

