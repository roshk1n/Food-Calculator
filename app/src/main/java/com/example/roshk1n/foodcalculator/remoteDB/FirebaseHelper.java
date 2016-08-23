package com.example.roshk1n.foodcalculator.remoteDB;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.interfaces.CreateUserFirebaseCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataAddFoodCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataFavoriteCallback;
import com.example.roshk1n.foodcalculator.interfaces.StateItemCallback;
import com.example.roshk1n.foodcalculator.interfaces.FirebaseCallback;
import com.example.roshk1n.foodcalculator.interfaces.LoadDayCallback;
import com.example.roshk1n.foodcalculator.interfaces.OnCompleteCallback;
import com.example.roshk1n.foodcalculator.interfaces.UserProfileCallback;
import com.example.roshk1n.foodcalculator.remoteDB.model.FoodFirebase;
import com.example.roshk1n.foodcalculator.remoteDB.model.UserFirebase;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.interfaces.ResponseListentenerUpload;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User;
import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FirebaseHelper {
    private static final String USERS_CHILD = "users";
    private static final String AGE_CHILD = "age";
    private static final String WEIGHT_CHILD = "weight";
    private static final String HEIGHT_CHILD = "height";
    private static final String SEX_CHILD = "sex";
    private static final String ACTIVE_CHILD = "activeLevel";
    private static final String GOAL_CALORIES_CHILD = "goalCalories";
    private static final String FAVORITE_FOOD_CHILD = "favoriteFoods";
    private static final String DAYS_CHILD = "days";
    private static final String FOODS_CHILD = "foods";

    private static FirebaseHelper instance;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUser mFirebaseUser;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseCallback firebaseCallback;

    private FirebaseHelper() {
    }

    private FirebaseHelper(FirebaseCallback callback) {
        this.firebaseCallback = callback;
    }

    public static FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public static FirebaseHelper getInstance(FirebaseCallback callback) {
        if (instance == null) {
            instance = new FirebaseHelper(callback);
        }
        return instance;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public void setStorage(FirebaseStorage storage) {
        this.storage = storage;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public FirebaseAuth.AuthStateListener getmAuthListner() {
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

    public void removeListner() {
        mAuth.removeAuthStateListener(mAuthListner);
    }

    public void addListner() {
        mAuth.addAuthStateListener(mAuthListner);
    }

    public void logInWithEmail(String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
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

    public void createUser(final String email,
                           final String password,
                           final String fullname,
                           final String urlPhoto,
                           final CreateUserFirebaseCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("MyLog", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullname)
                                    .setPhotoUri(Uri.parse(urlPhoto))
                                    .build();

                            mFirebaseUser.updateProfile(profileChangeRequest)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                final DatabaseReference reference = database.getReference()
                                                        .child("users")
                                                        .child(getmAuth().getCurrentUser().getUid());
                                                UserFirebase user = new UserFirebase();
                                                user.setAge(0l);
                                                user.setActiveLevel("none");
                                                user.setHeight(0l);
                                                user.setSex("none");
                                                user.setWeight(0l);
                                                user.setGoalCalories(0l);
                                                reference.setValue(user);
                                                callback.createSuccess();
                                            }
                                        }
                                    });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.createError(e.getMessage());
            }
        });
    }

    private User buildUserByJSON(JSONObject object) {
        User user = new User();
        String gender = "none";
        String birthday = "0";
        int old=0;
        try {
            if (object.has("email"))
                user.setEmail(object.getString("email"));
            if (object.has("picture"))
                user.setPhotoUrl(object.getJSONObject("picture").getJSONObject("data").getString("url"));
            if (object.has("name"))
                user.setFullname(object.getString("name"));
            if (object.has("gender"))
                gender = object.getString("gender");
            if (object.has("birthday"))
                birthday = object.getString("birthday");

            if (gender != null && gender.equals("male")) {
                gender = "Male";
            } else  if (gender != null) {
                gender = "Female";
            }

            if(!birthday.equals("0")) {
                DateFormat format = new SimpleDateFormat("M/d/yyyy");
                Date date = null;
                try {
                    date = format.parse(birthday);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date dateNow = new Date();
                old = dateNow.getYear() - date.getYear();
            }
            user.setSex(gender);
            user.setAge(old);
            user.setActiveLevel("none");
            user.setHeight(0);
            user.setWeight(0);
            user.setGoalCalories(0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void createProfileFacebook(final JSONObject object, final OnCompleteCallback callback) {
        final User user = buildUserByJSON(object);
        final UserFirebase userFirebase = new UserFirebase(user);
        final DatabaseReference reference = database.getReference()
                .child("users");
        reference.keepSynced(true);
        final DatabaseReference userRef = reference.child(getmAuth().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean check = false;
                Log.d("Myy",dataSnapshot.getChildrenCount()+"");
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    if(data.getKey().equals(getmAuth().getCurrentUser().getUid())) {
                        check = true;
                        break;
                    }
                }
                if(!check) {
                    userRef.setValue(userFirebase);
                }
//                Session.startSession();
//                Session.getInstance().setFullname(user.getFullname());
//                Session.getInstance().setEmail(user.getEmail());
//                Session.getInstance().setUrlPhoto(user.getPhotoUrl());
                callback.success();
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

    public void uploadImage(Bitmap image, String email, final ResponseListentenerUpload upload) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        StorageReference storage = getStorage()
                .getReferenceFromUrl("gs://food-calculator.appspot.com/");

        StorageReference mountainsRef = storage
                .child("images/profile/" + email + "_profile_photo");

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

    public void loadUserProfile(final UserProfileCallback callback) {
        DatabaseReference reference = database.getReference(USERS_CHILD);
        final DatabaseReference userRef = reference
                .child(getmAuth().getCurrentUser().getUid());

        userRef.keepSynced(true);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = new User();
                user.setEmail(getmAuth().getCurrentUser().getEmail());
                user.setFullname(getmAuth().getCurrentUser().getDisplayName());
                user.setPhotoUrl(getmAuth().getCurrentUser().getPhotoUrl().toString());
                user.setAge(Integer.valueOf(dataSnapshot.child(AGE_CHILD).getValue().toString()));
                user.setWeight(Integer.valueOf(dataSnapshot.child(WEIGHT_CHILD).getValue().toString()));
                user.setHeight(Integer.valueOf(dataSnapshot.child(HEIGHT_CHILD).getValue().toString()));
                user.setGoalCalories(Integer.valueOf(dataSnapshot.child(GOAL_CALORIES_CHILD).getValue().toString()));
                user.setSex(dataSnapshot.child(SEX_CHILD).getValue().toString());
                user.setActiveLevel(dataSnapshot.child(ACTIVE_CHILD).getValue().toString());
                callback.loadProfileSuccess(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void updateUserProfile(final User user, Bitmap image, final OnCompleteCallback callback) {
        UserFirebase userFire = new UserFirebase(user);
        DatabaseReference reference = database.getReference(USERS_CHILD);
        final DatabaseReference userRef = reference
                .child(getmAuth().getCurrentUser().getUid());
        userRef.keepSynced(true);

        userRef.child(AGE_CHILD).setValue(userFire.getAge());
        userRef.child(WEIGHT_CHILD).setValue(userFire.getWeight());
        userRef.child(HEIGHT_CHILD).setValue(userFire.getHeight());
        userRef.child(GOAL_CALORIES_CHILD).setValue(userFire.getGoalCalories());
        userRef.child(SEX_CHILD).setValue(userFire.getSex());
        userRef.child(ACTIVE_CHILD).setValue(userFire.getActiveLevel());

        uploadImage(image, user.getEmail(), new ResponseListentenerUpload() {
            @Override
            public void onSuccess(final String s) {
                mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(user.getFullname())
                        .setPhotoUri(Uri.parse(s))
                        .build();

                mFirebaseUser.updateProfile(profileChangeRequest)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                Session.getInstance().setEmail(user.getEmail());
                Session.getInstance().setFullname(user.getFullname());
                Session.getInstance().setUrlPhoto(s);
                callback.success();
     /*           mFirebaseUser.updateEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("myyy",task+"");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });*/
            }
        });
    }

    public void loadDay(final Date date, final LoadDayCallback loadDayCallback) {
        DatabaseReference reference = database.getReference(USERS_CHILD);
        final DatabaseReference foodRef = reference
                .child(getmAuth().getCurrentUser().getUid())
                .child(DAYS_CHILD)
                .child(date.getDate() + "_" + date.getMonth() + "_" + date.getYear())
                .child(FOODS_CHILD);
        foodRef.keepSynced(true);

        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Day day = new Day();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    day.getFoods().add(new Food(postSnapshot.getValue(FoodFirebase.class)));
                }
                day.setDate(date.getTime());
                loadDayCallback.loadComplete(day);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void addFood(Food food) {
        Date dateFood = new Date(food.getTime());
        DatabaseReference reference = database.getReference(USERS_CHILD);
        DatabaseReference foodsRef = reference
                .child(getmAuth().getCurrentUser().getUid())
                .child(DAYS_CHILD)
                .child(dateFood.getDate() + "_" + dateFood.getMonth() + "_" + dateFood.getYear())
                .child(FOODS_CHILD);
        foodsRef.keepSynced(true);
        foodsRef.child(food.getTime() + "").setValue(new FoodFirebase(food));
    }

    public void removeFood(long time) {
        DatabaseReference userRef = database.getReference(USERS_CHILD)
                .child(getmAuth().getCurrentUser().getUid());
        Date date = new Date(time);
        DatabaseReference foodRef = userRef.child(DAYS_CHILD)
                .child(date.getDate() + "_" + date.getMonth() + "_" + date.getYear())
                .child(FOODS_CHILD);
        foodRef.keepSynced(true);
        foodRef.child(time + "").removeValue();
    }

    public void loadFavoriteFood(final DataFavoriteCallback callback) {
        DatabaseReference reference = database.getReference(USERS_CHILD);
        DatabaseReference userRef = reference
                .child(getmAuth().getCurrentUser().getUid());
        final DatabaseReference favoriteRef = userRef.child(FAVORITE_FOOD_CHILD);
        userRef.keepSynced(true);
        favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Food> favFoods = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    favFoods.add(new Food(postSnapshot.getValue(FoodFirebase.class)));
                }
                callback.setFavoriteList(favFoods);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void addFavoriteFood(final Food food, final StateItemCallback callback) {
        DatabaseReference reference = database.getReference(USERS_CHILD);
        DatabaseReference userRef = reference
                .child(getmAuth().getCurrentUser().getUid());
        userRef.keepSynced(true);
        final DatabaseReference favoriteRef = userRef.child(FAVORITE_FOOD_CHILD);
        favoriteRef.child(food.getNdbno()).setValue(food);
        callback.updateImageFavorite(true);
    }

    public void removeFavoriteFood(final String ndbno, final StateItemCallback callback) {
        DatabaseReference reference = database.getReference(USERS_CHILD);
        DatabaseReference userRef = reference
                .child(getmAuth().getCurrentUser().getUid());
        final DatabaseReference favoriteRef = userRef.child(FAVORITE_FOOD_CHILD);
        favoriteRef.keepSynced(true);
        favoriteRef.child(ndbno).removeValue();
        callback.updateImageFavorite(false);
    }

    public void removeFavoriteFood(String ndbno) { //this method needs for swipe delete
        DatabaseReference reference = database.getReference(USERS_CHILD);
        DatabaseReference userRef = reference
                .child(getmAuth().getCurrentUser().getUid());
        DatabaseReference favoriteRef = userRef.child(FAVORITE_FOOD_CHILD);
        favoriteRef.keepSynced(true);
        favoriteRef.child(ndbno).removeValue();
    }

    public void isExistInFavorite(final Food food, final DataAddFoodCallback callback) {
        DatabaseReference reference = database.getReference(USERS_CHILD);
        DatabaseReference userRef = reference
                .child(getmAuth().getCurrentUser().getUid());
        DatabaseReference favoriteRef = userRef.child(FAVORITE_FOOD_CHILD);
        favoriteRef.keepSynced(true);
        favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean checkExistFood = false;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getValue(FoodFirebase.class).getNdbno().equals(food.getNdbno())) {
                        checkExistFood = true;
                        break;
                    }
                }
                callback.setExistFavorite(checkExistFood);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void loginFacebook(AccessToken access, final CreateUserFirebaseCallback callback) {
        AuthCredential credential = FacebookAuthProvider.getCredential(access.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.createSuccess();
                        } else {
                            callback.createError(task.getException().getMessage());
                        }
                    }
                });
    }
}

