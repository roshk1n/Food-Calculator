package com.example.roshk1n.foodcalculator.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.interfaces.OnFragmentListener;
import com.example.roshk1n.foodcalculator.presenters.ProfilePresenterImpl;
import com.example.roshk1n.foodcalculator.views.ProfileView;
import com.example.roshk1n.foodcalculator.utils.Utils;

public class ProfileFragment extends Fragment implements ProfileView, View.OnClickListener, View.OnFocusChangeListener {
    private static final int PICK_PHOTO_FOR_AVATAR = 0;
    private static final int MAKE_PHOTO = 1;

    private ProfilePresenterImpl profilePresenter;
    private OnFragmentListener mFragmentListener;

    private CoordinatorLayout coordinatorLayout;
    private View view;
    private ImageView profileIv;
    private EditText fullNameEt;
    private EditText ageEt;
    private EditText heightEt;
    private EditText weightEt;
    private EditText emailEt;
    private Spinner sexProfileSp;
    private Spinner activeLevelProfileSp;
    private ImageView fullNameIcoIv;
    private ImageView emailIcoIv;
    private ImageView ageIcoIv;
    private ImageView weightIcoIv;
    private ImageView heightIcoIv;
    private ImageView activeLevelIcoIv;
    private ImageView sexIcoProfileIv;
    private ProgressDialog saveDatePd;
    private FloatingActionButton editProfileFab;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profilePresenter = new ProfilePresenterImpl();
        profilePresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        initUI();

        fullNameEt.setOnFocusChangeListener(this);
        ageEt.setOnFocusChangeListener(this);
        heightEt.setOnFocusChangeListener(this);
        weightEt.setOnFocusChangeListener(this);
        emailEt.setOnFocusChangeListener(this);

        if (mFragmentListener != null) {
            mFragmentListener.setTitle(getString(R.string.profile));
        }

        profilePresenter.loadUser();
        profileIv.setOnClickListener(this);
        editProfileFab.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            mFragmentListener = (OnFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        mFragmentListener = null;
        profilePresenter.destroy();
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            profilePresenter.setUserPhotoSD(data, getActivity().getApplicationContext().getContentResolver());
        }
        if (requestCode == MAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            profilePresenter.setUserPhotoCamera(data);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == profileIv) {
            if (fullNameEt.isEnabled()) {
                createPickerPhoto();
            } else {
                Snackbar.make(coordinatorLayout, R.string.turn_edit_mode, Snackbar.LENGTH_SHORT).show();
            }

        } else if (v == editProfileFab) {
            if (Utils.isConnectNetwork(getActivity().getApplicationContext())) {
                if (fullNameEt.isEnabled()) {
                    saveDatePd.setMessage(getString(R.string.wait_please));
                    saveDatePd.setIndeterminate(true);
                    saveDatePd.show();
                    saveUserProfile();
                }
                changeEnable(fullNameEt.isEnabled());
                changeIcon();
            } else {
                Snackbar.make(coordinatorLayout, R.string.need_connection_update, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void setProfile(Bitmap BitmapImage,
                           String email,
                           String fullname,
                           int age,
                           int height,
                           int weight,
                           String sex,
                           String activeLevel) {

        fullNameEt.setText(fullname);
        weightEt.setText(String.valueOf(weight));
        heightEt.setText(String.valueOf(height));
        ageEt.setText(String.valueOf(age));
        emailEt.setText(email);
        int positionActive = profilePresenter.getPositionInArray(activeLevel,
                getResources().getStringArray(R.array.active_level));

        int positionSex = profilePresenter.getPositionInArray(sex,
                getResources().getStringArray(R.array.sex));
        activeLevelProfileSp.setSelection(positionActive);
        sexProfileSp.setSelection(positionSex);
        profileIv.setImageBitmap(BitmapImage);
    }

    @Override
    public void setUserPhoto(Bitmap bitmap) {
        profileIv.setImageBitmap(bitmap);
    }

    @Override
    public void CompleteUpdateAndRefreshDrawer() {
        mFragmentListener.updateDrawerLight();
        saveDatePd.dismiss();
        Snackbar.make(coordinatorLayout, R.string.user_saved, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String s) {
        saveDatePd.dismiss();
        changeEnable(fullNameEt.isEnabled());
        changeIcon();
        Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_SHORT).show();
    }

    private void createPickerPhoto() {
        profilePresenter.getPermissions();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.photo))
                .setItems(R.array.photo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            getIntent.setType("image/*");

                            Intent pickIntent = new Intent(Intent.ACTION_PICK
                                    , android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            pickIntent.setType("image/*");

                            Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.select_image));
                            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                            startActivityForResult(chooserIntent, PICK_PHOTO_FOR_AVATAR);
                        } else {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, MAKE_PHOTO);
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveUserProfile() {
        Bitmap bitmap = ((BitmapDrawable) profileIv.getDrawable()).getBitmap();
        if (bitmap != null) {
            profilePresenter.updateUserProfile(
                    fullNameEt.getText().toString(),
                    weightEt.getText().toString(),
                    heightEt.getText().toString(),
                    ageEt.getText().toString(),
                    emailEt.getText().toString(),
                    bitmap,
                    sexProfileSp.getSelectedItem().toString(),
                    activeLevelProfileSp.getSelectedItem().toString()
            );

        } else {
            saveDatePd.dismiss();
            showToast(getString(R.string.image_incorrect));
            profileIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.profile_default));
        }
    }

    private void changeIcon() {
        if (fullNameEt.isEnabled()) {
            fullNameIcoIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_person_accent_24dp));
            emailIcoIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mail_accent_24dp));
            ageIcoIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_age_accent_24dp));
            weightIcoIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_weight_accent_24dp));
            heightIcoIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_ruler_accent_24dp));
            activeLevelIcoIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_active_level_accent_24dp));
            sexIcoProfileIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_male_female_accent_24dp));
        } else {
            fullNameIcoIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_person_primary_24dp));
            emailIcoIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mail_primary_24dp));
            ageIcoIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_age_primary_24dp));
            weightIcoIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_weight_primary_24dp));
            heightIcoIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_ruler_primary_24dp));
            activeLevelIcoIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_active_level_primary_24dp));
            sexIcoProfileIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_male_female_primary_24dp));
        }
    }

    private void changeEnable(boolean isEnable) {
        fullNameEt.setEnabled(!isEnable); //set enable or disable change
        weightEt.setEnabled(!isEnable);
        heightEt.setEnabled(!isEnable);
        ageEt.setEnabled(!isEnable);
        emailEt.setEnabled(!isEnable);
        sexProfileSp.setEnabled(!isEnable);
        activeLevelProfileSp.setEnabled(!isEnable);

        if (!isEnable) {
            editProfileFab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_done_white_24dp));
            Snackbar.make(coordinatorLayout, R.string.remember_enter_field, Snackbar.LENGTH_SHORT).show();

        } else {
            fullNameEt.clearFocus(); // clear focus after saving
            weightEt.clearFocus();
            heightEt.clearFocus();
            emailEt.clearFocus();
            ageEt.clearFocus();
            sexProfileSp.clearFocus();
            activeLevelProfileSp.clearFocus();
            editProfileFab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_create_white_24dp));
        }
    }

    private void initUI() {
        editProfileFab = (FloatingActionButton) view.findViewById(R.id.edit_profile_fab);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.profile_coordinator_layout);

        fullNameEt = (EditText) view.findViewById(R.id.full_name_profile_et);
        weightEt = (EditText) view.findViewById(R.id.weight_profile_et);
        heightEt = (EditText) view.findViewById(R.id.height_profile_et);
        ageEt = (EditText) view.findViewById(R.id.age_profile_et);
        emailEt = (EditText) view.findViewById(R.id.email_profile_et);

        sexProfileSp = (Spinner) view.findViewById(R.id.sex_profile_sp);
        activeLevelProfileSp = (Spinner) view.findViewById(R.id.active_level_profile_sp);

        profileIv = (ImageView) view.findViewById(R.id.profile_iv);
        fullNameIcoIv = (ImageView) view.findViewById(R.id.full_name_ico_profile_iv);
        emailIcoIv = (ImageView) view.findViewById(R.id.email_ico_profile_iv);
        ageIcoIv = (ImageView) view.findViewById(R.id.age_ico_profile_iv);
        weightIcoIv = (ImageView) view.findViewById(R.id.weight_ico_profile_iv);
        heightIcoIv = (ImageView) view.findViewById(R.id.height_ico_profile_iv);
        activeLevelIcoIv = (ImageView) view.findViewById(R.id.active_level_ico_profile_iv);
        sexIcoProfileIv = (ImageView) view.findViewById(R.id.sex_ico_profile_iv);

        sexProfileSp.setEnabled(fullNameEt.isEnabled());
        activeLevelProfileSp.setEnabled(fullNameEt.isEnabled());

        saveDatePd = new ProgressDialog(getContext());
        saveDatePd.setCanceledOnTouchOutside(false);
        saveDatePd.setCancelable(false);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            Utils.hideKeyboard(getContext(), v);
            v.clearFocus();
        }
        if (hasFocus)
            Utils.showKeyboard(getContext(), v);
    }
}
