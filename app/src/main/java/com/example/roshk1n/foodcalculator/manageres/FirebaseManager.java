package com.example.roshk1n.foodcalculator.manageres;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.interfaces.CreateUserFirebaseCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataAddFoodCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataFavoriteCallback;
import com.example.roshk1n.foodcalculator.interfaces.FirebaseCallback;
import com.example.roshk1n.foodcalculator.interfaces.LoadDaysCallback;
import com.example.roshk1n.foodcalculator.interfaces.LoginCallback;
import com.example.roshk1n.foodcalculator.interfaces.ResetPasswordCallback;
import com.example.roshk1n.foodcalculator.interfaces.StateItemCallback;
import com.example.roshk1n.foodcalculator.interfaces.LoadDayCallback;
import com.example.roshk1n.foodcalculator.interfaces.OnCompleteCallback;
import com.example.roshk1n.foodcalculator.interfaces.UserProfileCallback;
import com.example.roshk1n.foodcalculator.remoteDB.model.FoodFirebase;
import com.example.roshk1n.foodcalculator.remoteDB.model.UserFirebase;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.interfaces.ResponseListenerUpload;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User;
import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FirebaseManager {
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
    private static final String EAT_CALORIES = "eatCalories";
    private static final String REMAINING_CALORIES = "remainingCalories";
    private static FirebaseManager instance;

    private ArrayList<Day> listDay = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseCallback firebaseCallback;

    private FirebaseManager() {
    }

    private FirebaseManager(FirebaseCallback callback) {
        this.firebaseCallback = callback;
    }

    public static FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    public static FirebaseManager getInstance(FirebaseCallback callback) {
        if (instance == null) {
            instance = new FirebaseManager(callback);
        }
        return instance;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public void setStorage(FirebaseStorage storage) {
        this.storage = storage;
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public void setAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public FirebaseUser getFirebaseUser() {
        return mFirebaseUser;
    }

    public void setFirebaseUser(FirebaseUser mFirebaseUser) {
        this.mFirebaseUser = mFirebaseUser;
    }

    public void logInWithEmail(String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    setFirebaseUser(getAuth().getCurrentUser());
                    Session.startSession();
                    Session.getInstance().setEmail(getFirebaseUser().getEmail());
                    Session.getInstance().setFullname(getFirebaseUser().getDisplayName());
                    Session.getInstance().setUrlPhoto(String.valueOf(getFirebaseUser().getPhotoUrl()));
                    firebaseCallback.loginSuccessful();

                } else {
                    firebaseCallback.loginError("Authentication failed. Try again please!");
                }
            }
        });
    }

    public void resetPassword(String email, final ResetPasswordCallback callback) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.resetSuccess("New password sent successful.");
                        } else {
                            callback.resetError("Error reset password.");
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
                                                        .child(getAuth().getCurrentUser().getUid());
                                                UserFirebase user = new UserFirebase();
                                                user.setAge(0L);
                                                user.setActiveLevel("none");
                                                user.setHeight(0L);
                                                user.setSex("none");
                                                user.setWeight(0L);
                                                user.setGoalCalories(0L);
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
        int old = 0;
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
            } else if (gender != null) {
                gender = "Female";
            }

            if (!birthday.equals("0")) {
                SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy");
                Calendar date = Calendar.getInstance();
                try {
                    date.setTime(format.parse(birthday));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar dateNow = Calendar.getInstance();

                old = dateNow.get(Calendar.YEAR) - date.get(Calendar.YEAR);
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

    public void createProfileFacebook(final JSONObject object, final UserProfileCallback callback) {
        final User user = buildUserByJSON(object);
        final UserFirebase userFirebase = new UserFirebase(user);
        final DatabaseReference reference = database.getReference()
                .child("users");
        reference.keepSynced(true);

        final DatabaseReference userRef = reference.child(getAuth().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean check = false;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getKey().equals(getAuth().getCurrentUser().getUid())) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    userRef.setValue(userFirebase);
                }
                callback.loadProfileSuccess(user);
                Session.startSession();
                Session.getInstance().setEmail(user.getEmail());
                Session.getInstance().setFullname(user.getFullname());
                Session.getInstance().setUrlPhoto(user.getPhotoUrl());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void checkLogin(final LoginCallback callback) {
        setAuth(FirebaseAuth.getInstance());
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            setFirebaseUser(getAuth().getCurrentUser());
            Session.startSession();
            Session.getInstance().setEmail(user.getEmail());
            Session.getInstance().setFullname(user.getDisplayName());
            Session.getInstance().setUrlPhoto(String.valueOf(user.getPhotoUrl()));
            callback.loginSuccess();
        }
    }

    public void uploadImage(Bitmap image, String email, final ResponseListenerUpload upload) {
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseReference reference = database.getReference(USERS_CHILD);
                final DatabaseReference userRef = reference
                        .child(getAuth().getCurrentUser().getUid());

                userRef.keepSynced(true);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = new User();
                        user.setEmail(getAuth().getCurrentUser().getEmail());
                        user.setFullname(getAuth().getCurrentUser().getDisplayName());
                        user.setPhotoUrl(getAuth().getCurrentUser().getPhotoUrl().toString());
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
        }).start();
    }

    public void updateUserProfile(final User user, Bitmap image, final OnCompleteCallback callback) {
        UserFirebase userFire = new UserFirebase(user);
        DatabaseReference reference = database.getReference(USERS_CHILD);
        final DatabaseReference userRef = reference
                .child(getAuth().getCurrentUser().getUid());
        userRef.keepSynced(true);

        userRef.child(AGE_CHILD).setValue(userFire.getAge());
        userRef.child(WEIGHT_CHILD).setValue(userFire.getWeight());
        userRef.child(HEIGHT_CHILD).setValue(userFire.getHeight());
        userRef.child(GOAL_CALORIES_CHILD).setValue(userFire.getGoalCalories());
        userRef.child(SEX_CHILD).setValue(userFire.getSex());
        userRef.child(ACTIVE_CHILD).setValue(userFire.getActiveLevel());

        uploadImage(image, user.getEmail(), new ResponseListenerUpload() {
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
                mFirebaseUser.updateEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

            }
        });
    }

    public void loadDay(final Calendar date, final LoadDayCallback loadDayCallback) {
        DatabaseReference reference = database.getReference(USERS_CHILD);
        final DatabaseReference foodRef = reference
                .child(getAuth().getCurrentUser().getUid())
                .child(DAYS_CHILD)
                .child(date.get(Calendar.YEAR) + "_" + date.get(Calendar.MONTH) + "_" + date.get(Calendar.DAY_OF_MONTH))
                .child(FOODS_CHILD);
        foodRef.keepSynced(true);

        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Day day = new Day();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    day.getFoods().add(new Food(postSnapshot.getValue(FoodFirebase.class)));
                }
                day.setDate(date.getTime().getTime());
                loadDayCallback.loadComplete(day);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void addFood(Food food) {
        Calendar dateFood = Calendar.getInstance();
        dateFood.setTimeInMillis(food.getTime());
        DatabaseReference reference = database.getReference(USERS_CHILD);
        DatabaseReference foodsRef = reference
                .child(getAuth().getCurrentUser().getUid())
                .child(DAYS_CHILD)
                .child(dateFood.get(Calendar.YEAR) + "_" + dateFood.get(Calendar.MONTH) + "_" + dateFood.get(Calendar.DAY_OF_MONTH))
                .child(FOODS_CHILD);
        foodsRef.keepSynced(true);
        foodsRef.child(food.getTime() + "").setValue(new FoodFirebase(food));
    }

    public void removeFood(long time) {
        DatabaseReference userRef = database.getReference(USERS_CHILD)
                .child(getAuth().getCurrentUser().getUid());
        Calendar dateFood = Calendar.getInstance();
        dateFood.setTimeInMillis(time);
        DatabaseReference foodRef = userRef.child(DAYS_CHILD)
                .child(dateFood.get(Calendar.YEAR) + "_" + dateFood.get(Calendar.MONTH) + "_" + dateFood.get(Calendar.DAY_OF_MONTH))
                .child(FOODS_CHILD);
        foodRef.keepSynced(true);
        foodRef.child(time + "").removeValue();
    }

    public void loadFavoriteFood(final DataFavoriteCallback callback) {
        DatabaseReference reference = database.getReference(USERS_CHILD);
        DatabaseReference userRef = reference
                .child(getAuth().getCurrentUser().getUid());
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
                .child(getAuth().getCurrentUser().getUid());
        userRef.keepSynced(true);
        final DatabaseReference favoriteRef = userRef.child(FAVORITE_FOOD_CHILD);
        favoriteRef.child(food.getNdbno()).setValue(food);
        callback.updateImageFavorite(true);
    }

    public void removeFavoriteFood(final String ndbno, final StateItemCallback callback) {
        DatabaseReference reference = database.getReference(USERS_CHILD);
        DatabaseReference userRef = reference
                .child(getAuth().getCurrentUser().getUid());
        final DatabaseReference favoriteRef = userRef.child(FAVORITE_FOOD_CHILD);
        favoriteRef.keepSynced(true);
        favoriteRef.child(ndbno).removeValue();
        callback.updateImageFavorite(false);
    }

    public void isExistInFavorite(final Food food, final DataAddFoodCallback callback) {
        DatabaseReference reference = database.getReference(USERS_CHILD);
        DatabaseReference userRef = reference
                .child(getAuth().getCurrentUser().getUid());
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

    public void loadDataForChart(final LoadDaysCallback callback) {
        DatabaseReference reference = database.getReference(USERS_CHILD);
        final DatabaseReference daysRef = reference
                .child(getAuth().getCurrentUser().getUid())
                .child(DAYS_CHILD);
        listDay.clear();
        daysRef.keepSynced(true);
        daysRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot oneDay : dataSnapshot.getChildren()) {
                    Day day = new Day();
                    String[] s = oneDay.getKey().split("_");
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, Integer.valueOf(s[0]));
                    calendar.set(Calendar.MONTH, Integer.valueOf(s[1]));
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(s[2]));
                    day.setDate(calendar.getTime().getTime());
                    day.setEatDailyCalories(Integer.valueOf(oneDay.child(EAT_CALORIES).getValue().toString()));
                    day.setRemainingCalories(Integer.valueOf(oneDay.child(REMAINING_CALORIES).getValue().toString()));
                    for (DataSnapshot food : oneDay.child(FOODS_CHILD).getChildren()) {
                        day.getFoods().add(new Food(food.getValue(FoodFirebase.class)));
                    }
                    listDay.add(day);
                }
                callback.loadDaysComplete(listDay);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateCalories(final int eat_calories, final int remainingCalories, long date) {
        Calendar dateFood = Calendar.getInstance();
        dateFood.setTimeInMillis(date);
        DatabaseReference reference = database.getReference(USERS_CHILD);
        final DatabaseReference refDay = reference.child(getAuth().getCurrentUser().getUid())
                .child(DAYS_CHILD)
                .child(dateFood.get(Calendar.YEAR) + "_" + dateFood.get(Calendar.MONTH) + "_" + dateFood.get(Calendar.DAY_OF_MONTH));
        refDay.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    refDay.child(EAT_CALORIES).setValue(eat_calories + "");
                    refDay.child(REMAINING_CALORIES).setValue(remainingCalories + "");
                    if (dataSnapshot.getChildrenCount() == 2) {
                        refDay.setValue(null);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

