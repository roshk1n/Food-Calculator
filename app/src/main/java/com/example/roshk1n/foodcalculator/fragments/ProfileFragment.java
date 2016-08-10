package com.example.roshk1n.foodcalculator.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.activities.MainActivity;
import com.example.roshk1n.foodcalculator.presenters.ProfilePresenterImpl;
import com.example.roshk1n.foodcalculator.Views.ProfileView;

public class ProfileFragment extends Fragment implements ProfileView {

    private static final int PICK_PHOTO_FOR_AVATAR = 0;
    private static final int MAKE_PHOTO = 1;

    private ProfilePresenterImpl profilePresenter;

    private CoordinatorLayout coordinatorLayout;
    private View view;
    private ImageView profile_iv;
    private EditText full_name_et;
    private EditText age_et;
    private EditText height_et;
    private EditText weight_et;
    private EditText email_et;
    private Spinner sex_profile_sp;
    private Spinner active_level__profile_sp;
    private ImageView full_name_ico_iv;
    private ImageView email_ico_iv;
    private ImageView age_ico_iv;
    private ImageView weight_ico_iv;
    private ImageView height_ico_iv;
    private ImageView active_level_ico_iv;
    private ImageView sex_ico_profile_iv;

    private FloatingActionButton edit_profile_fab;

    public ProfileFragment() { }

    public static ProfileFragment newInstance() { return new ProfileFragment(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profilePresenter = new ProfilePresenterImpl();
        profilePresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_profile, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");

        initUI();

        profilePresenter.getUserProfile();

        profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (full_name_et.isEnabled()) {
                    createPickerPhoto();
                } else {
                    Snackbar.make(coordinatorLayout, "First, turn on editing mode.", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        edit_profile_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile(full_name_et.isEnabled());
                changeEnable(full_name_et.isEnabled());
                changeIcon();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_PHOTO_FOR_AVATAR&& resultCode== Activity.RESULT_OK) {
            profilePresenter.setUserPhotoSD(data,getActivity().getApplicationContext().getContentResolver());
        }
        if(requestCode==MAKE_PHOTO && resultCode==Activity.RESULT_OK) {
            profilePresenter.setUserPhotoCamera(data);
        }
    }

    @Override
    public void setProfile(String photoUrl, String email, String fullname, int age, int height, int weight, String sex, String activeLevel) {
        full_name_et.setText(fullname);
        weight_et.setText(String.valueOf(weight));
        height_et .setText(String .valueOf(height));
        age_et.setText(String.valueOf(age));
        email_et.setText(email);
        int positionActive = profilePresenter.getPositionInArray(activeLevel,
                getResources().getStringArray(R.array.active_level));

        int positionSex = profilePresenter.getPositionInArray(sex,
                getResources().getStringArray(R.array.sex));

        active_level__profile_sp.setSelection(positionActive);
        sex_profile_sp.setSelection(positionSex);
        profile_iv.setImageBitmap( profilePresenter.stringToBitmap(photoUrl) ); //convert to Bitmap
    }

    @Override
    public void setUserPhoto(Bitmap bitmap) {

        profile_iv.setImageBitmap(bitmap);
    }

    @Override
    public void CompleteUpdateAndRefreshDrawer() {
        ((MainActivity)view.getContext()).updateDrawer();
        Snackbar.make(coordinatorLayout, "User data saved successfully.", Snackbar.LENGTH_SHORT).show();
    }

    private void createPickerPhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    private void saveUserProfile(boolean isEnable) {
        if (isEnable) {
            profile_iv.setDrawingCacheEnabled(true);// get Bitmap from ImageView
            profile_iv.buildDrawingCache();
            profilePresenter.updateUserProfile(
                    full_name_et.getText().toString(),
                    weight_et.getText().toString(),
                    height_et.getText().toString(),
                    age_et.getText().toString(),
                    email_et.getText().toString(),
                    profile_iv.getDrawingCache(),
                    sex_profile_sp.getSelectedItem().toString(),
                    active_level__profile_sp.getSelectedItem().toString()
            );

            profilePresenter.updateLimitCalories();
        }
    }

    private void changeIcon() {
        if(full_name_et.isEnabled()) {
            full_name_ico_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_person_accent_24dp));
            email_ico_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_mail_accent_24dp));
            age_ico_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_age_accent_24dp));
            weight_ico_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_weight_accent_24dp));
            height_ico_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_ruler_accent_24dp));
            active_level_ico_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_active_level_accent_24dp));
            sex_ico_profile_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_male_female_accent_24dp));
        } else {
            full_name_ico_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_person_primary_24dp));
            email_ico_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_mail_primary_24dp));
            age_ico_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_age_primary_24dp));
            weight_ico_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_weight_primary_24dp));
            height_ico_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_ruler_primary_24dp));
            active_level_ico_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_active_level_primary_24dp));
            sex_ico_profile_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_male_female_primary_24dp));
        }
    }

    private void changeEnable(boolean isEnable) {
        full_name_et.setEnabled(!isEnable); //set enable or disable change
        weight_et.setEnabled(!isEnable);
        height_et.setEnabled(!isEnable);
        age_et.setEnabled(!isEnable);
        email_et.setEnabled(!isEnable);
        sex_profile_sp.setEnabled(!isEnable);
        active_level__profile_sp.setEnabled(!isEnable);

        if(!isEnable) {
            edit_profile_fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_done_white_24dp));
            Snackbar.make(coordinatorLayout, "You can edit data, but remember save them.", Snackbar.LENGTH_SHORT).show();
        } else {
            full_name_et.clearFocus(); // clear focus after saving
            weight_et.clearFocus();
            height_et.clearFocus();
            email_et.clearFocus();
            age_et.clearFocus();
            sex_profile_sp.clearFocus();
            active_level__profile_sp.clearFocus();
            edit_profile_fab.setImageDrawable(getActivity()
                    .getResources()
                    .getDrawable(R.drawable.ic_create_white_24dp));
        }
    }

    private void initUI() {
        edit_profile_fab = (FloatingActionButton) view.findViewById(R.id.edit_profile_fab);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.profile_coordinator_layout);

        full_name_et = (EditText) view.findViewById(R.id.full_name_profile_et);
        weight_et = (EditText) view.findViewById(R.id.weight_profile_et);
        height_et = (EditText) view.findViewById(R.id.height_profile_et);
        age_et = (EditText) view.findViewById(R.id.age_profile_et);
        email_et = (EditText) view.findViewById(R.id.email_profile_et);

        sex_profile_sp = (Spinner) view.findViewById(R.id.sex_profile_sp) ;
        active_level__profile_sp = (Spinner) view.findViewById(R.id.active_level_profile_sp) ;

        profile_iv = (ImageView) view.findViewById(R.id.profile_iv);
        full_name_ico_iv = (ImageView) view.findViewById(R.id.full_name_ico_profile_iv);
        email_ico_iv = (ImageView) view.findViewById(R.id.email_ico_profile_iv);
        age_ico_iv = (ImageView) view.findViewById(R.id.age_ico_profile_iv);
        weight_ico_iv = (ImageView) view.findViewById(R.id.weight_ico_profile_iv);
        height_ico_iv = (ImageView) view.findViewById(R.id.height_ico_profile_iv);
        active_level_ico_iv = (ImageView) view.findViewById(R.id.active_level_ico_profile_iv);
        sex_ico_profile_iv = (ImageView) view.findViewById(R.id.sex_ico_profile_iv);

        sex_profile_sp.setEnabled(full_name_et.isEnabled());
        active_level__profile_sp.setEnabled(full_name_et.isEnabled());
    }
}
