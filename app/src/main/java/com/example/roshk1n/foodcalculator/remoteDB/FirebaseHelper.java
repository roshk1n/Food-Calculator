package com.example.roshk1n.foodcalculator.remoteDB;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.interfaces.CreateUserFirebaseCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataAddFoodCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataFavoriteCalback;
import com.example.roshk1n.foodcalculator.interfaces.StateItemCallback;
import com.example.roshk1n.foodcalculator.interfaces.FirebaseCallback;
import com.example.roshk1n.foodcalculator.interfaces.LoadDayCallback;
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
import com.google.firebase.database.ChildEventListener;
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
    private static FirebaseHelper instance;
    private Date date = new Date();
    private DayFirebase dayFirebase = new DayFirebase();
    private List<FoodFirebase> foodsUpdate = new ArrayList<>();
    private List<FoodFirebase> favoriteFoodUpdate = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUser mFirebaseUser;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseCallback firebaseCallback;

    private FirebaseHelper() {}

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
                                                UserFirebase user = new UserFirebase();
                                                user.setAge(0l);
                                                user.setActiveLevel("none");
                                                user.setHeight(0l);
                                                user.setSex("none");
                                                user.setWeight(0l);
                                                user.setGoalCalories(0l);
                                                DatabaseReference reference = database.getReference()
                                                        .child("users")
                                                        .child(getmAuth().getCurrentUser().getUid());
                                                reference.setValue(user);
                                                callback.createSuccess();
                                            }
                                        }
                                    });
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

    public void addFood(Food food) {
        Date dateFood = new Date(food.getTime());
        dayFirebase = new DayFirebase();
        if (date.getDate() == dateFood.getDate()
                && date.getMonth() == dateFood.getMonth()
                && date.getYear() == dateFood.getYear()) {
            dayFirebase.setFoods(foodsUpdate); //TODO check if need
            dayFirebase.getFoods().add(new FoodFirebase(food));
        } else {
            dayFirebase.setFoods(foodsUpdate);
            dayFirebase.getFoods().add(new FoodFirebase(food));
            date = dateFood;
        }

        DatabaseReference reference = database.getReference("users");
        DatabaseReference userRef = reference
                .child(getmAuth().getCurrentUser().getUid())
                .child("days")
                .child(dateFood.getDate() + "_" + dateFood.getMonth() + "_" + dateFood.getYear());

        dayFirebase.setDate(food.getTime());
        userRef.setValue(dayFirebase);
    }

    public void removeFood(int position) {
        DatabaseReference reference = database.getReference("users");
        Date date = new Date(dayFirebase.getDate());
        DatabaseReference userRef = reference.child(getmAuth().getCurrentUser().getUid()).child("days").child(date.getDate() + "_" + date.getMonth() + "_" + date.getYear());
        userRef.child("foods").child(position + "").removeValue();
        dayFirebase.getFoods().remove(position);
        foodsUpdate.remove(position);
        userRef.setValue(dayFirebase);
    }

    public void loadFavoriteFood(final DataFavoriteCalback callback) {
        DatabaseReference reference = database.getReference("users");
        DatabaseReference userRef = reference
                .child(getmAuth().getCurrentUser().getUid());
        final DatabaseReference favoriteRef = userRef.child("favoriteFoods");

        favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favoriteFoodUpdate.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    favoriteFoodUpdate.add(postSnapshot.getValue(FoodFirebase.class));
                }

                ArrayList<Food> favFoods = new ArrayList<Food>();
                for (int i = 0; i < favoriteFoodUpdate.size(); i++) {
                    favFoods.add(new Food(favoriteFoodUpdate.get(i)));
                }
                favoriteRef.setValue(favoriteFoodUpdate);
                callback.setFavoriteList(favFoods);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void addFavoriteFood(final Food food, final StateItemCallback callback) {
        DatabaseReference reference = database.getReference("users");
        DatabaseReference userRef = reference
                .child(getmAuth().getCurrentUser().getUid());
        final DatabaseReference favoriteRef = userRef.child("favoriteFoods");

        favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favoriteFoodUpdate.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    favoriteFoodUpdate.add(postSnapshot.getValue(FoodFirebase.class));
                }
                favoriteFoodUpdate.add(new FoodFirebase(food));
                favoriteRef.setValue(favoriteFoodUpdate);
                callback.updateImageFavorite(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void removeFavoriteFood(final String ndbno, final StateItemCallback callback) {
        DatabaseReference reference = database.getReference("users");
        DatabaseReference userRef = reference
                .child(getmAuth().getCurrentUser().getUid());
        final DatabaseReference favoriteRef = userRef.child("favoriteFoods");

        favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    if(dataSnapshot.child(i+"").getValue(FoodFirebase.class).getNdbno().equals(ndbno)) {
                        favoriteFoodUpdate.remove(i);
                    }
                }
                favoriteRef.setValue(favoriteFoodUpdate);
                callback.updateImageFavorite(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void removeFavoriteFood(int position) {
        DatabaseReference reference = database.getReference("users");
        DatabaseReference userRef = reference
                .child(getmAuth().getCurrentUser().getUid());
        DatabaseReference favoriteRef = userRef.child("favoriteFoods");

        favoriteRef.child(position + "").removeValue();
        favoriteFoodUpdate.remove(position);

        favoriteRef.setValue(favoriteFoodUpdate);
    }

    public void isExistInFavorite(final Food food, final DataAddFoodCallback callback) {
        DatabaseReference reference = database.getReference("users");
        DatabaseReference userRef = reference
                .child(getmAuth().getCurrentUser().getUid());
        DatabaseReference favoriteRef = userRef.child("favoriteFoods");

        favoriteFoodUpdate.clear();
        favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean checkExistFood = false;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    favoriteFoodUpdate.add(postSnapshot.getValue(FoodFirebase.class));
                    if(postSnapshot.getValue(FoodFirebase.class).getNdbno().equals(food.getNdbno())) {
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

    public void loadDay(final Date date, final LoadDayCallback loadDayCallback) {
        DatabaseReference reference = database.getReference("users");
        final DatabaseReference dayRef = reference
                .child(getmAuth().getCurrentUser().getUid())
                .child("days")
                .child(date.getDate() + "_" + date.getMonth() + "_" + date.getYear());
        final DatabaseReference foodRef = dayRef.child("foods");

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
                }
                day.setFoods(foods);

                dayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        day.setDate(date.getTime());
                        dayFirebase = new DayFirebase(day);
                        loadDayCallback.loadComplete(day);
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
        foodsUpdate.clear();
        final Day day = new Day();
        foodRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("mmmmmm","jj");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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

    public void updateCalories(int eat_calories, int remainingCalories, long date) {
        Date date1 = new Date(date);
        DatabaseReference reference = database.getReference("users");
        final DatabaseReference dayRef = reference
                .child(getmAuth().getCurrentUser().getUid())
                .child("days")
                .child(date1.getDate() + "_" + date1.getMonth() + "_" + date1.getYear());
        dayRef.child("eatDailyCalories").setValue(eat_calories);
        dayRef.child("remainingCalories").setValue(remainingCalories);
    }

}

