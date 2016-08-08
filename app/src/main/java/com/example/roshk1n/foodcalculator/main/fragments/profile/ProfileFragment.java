package com.example.roshk1n.foodcalculator.main.fragments.profile;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.Session;

public class ProfileFragment extends Fragment implements ProfileView {

    private ProfilePresenterImpl profilePresenter;

    private View view;
    private ImageView profile_iv;
    private EditText full_name_et;
    private EditText age_et;
    private EditText height_et;
    private EditText weight_et;
    private EditText email_et;

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
        getActivity().getCurrentFocus().clearFocus();

        initUI();

        profilePresenter.getUserProfile();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");

        edit_profile_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!full_name_et.isEnabled()) {
                    full_name_et.setEnabled(true);
                    weight_et.setEnabled(true);
                    height_et.setEnabled(true);
                    age_et.setEnabled(true);
                    email_et.setEnabled(true);
                    edit_profile_fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_done_white_24dp));

                } else {
                    full_name_et.setEnabled(false);
                    weight_et.setEnabled(false);
                    height_et.setEnabled(false);
                    age_et.setEnabled(false);
                    email_et.setEnabled(false);
                    full_name_et.clearFocus();
                    weight_et.clearFocus();
                    height_et.clearFocus();
                    email_et.clearFocus();
                    age_et.clearFocus();
                    edit_profile_fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_create_white_24dp));
                    profilePresenter.updateUserProfile(
                            full_name_et.getText().toString(),
                            weight_et.getText().toString(),
                            height_et.getText().toString(),
                            age_et.getText().toString(),
                            email_et.getText().toString()
                            );
                }
            }
        });
        return view;
    }

    @Override
    public void setProfile(String photoUrl, String email, String fullname, int age, int height, int weight, String activeLevel) {
        full_name_et.setText(fullname);
        weight_et.setText(String.valueOf(weight));
        height_et .setText(String .valueOf(height));
        age_et.setText(String.valueOf(age));
        email_et.setText(email);

        Bitmap imageUser = profilePresenter.stringToBitmap(photoUrl);

        profile_iv.setImageBitmap(imageUser);
    }

    private void initUI() {
        full_name_et = (EditText) view.findViewById(R.id.full_name_profile_et);
        weight_et = (EditText) view.findViewById(R.id.weight_profile_et);
        height_et = (EditText) view.findViewById(R.id.height_profile_et);
        age_et = (EditText) view.findViewById(R.id.age_profile_et);
        email_et = (EditText) view.findViewById(R.id.email_profile_et);
        edit_profile_fab = (FloatingActionButton) view.findViewById(R.id.edit_profile_fab);
        profile_iv = (ImageView) view.findViewById(R.id.profile_iv);
    }
}
