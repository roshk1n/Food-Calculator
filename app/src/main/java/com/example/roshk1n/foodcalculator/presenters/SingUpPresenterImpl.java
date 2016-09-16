package com.example.roshk1n.foodcalculator.presenters;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.interfaces.DataSingUpCallback;
import com.example.roshk1n.foodcalculator.manageres.DataManager;
import com.example.roshk1n.foodcalculator.views.SingUpView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SingUpPresenterImpl implements SingUpPresenter, DataSingUpCallback {
    private SingUpView singUpView;
    private DataManager dataManager = new DataManager(this);

    @Override
    public void setView(SingUpView view) {
        singUpView = view;
    }

    @Override
    public void singUp(final String fullname, final String email, final String password, final String confirmPassword) {
        boolean error = false;
        if (TextUtils.isEmpty(fullname)) {
            singUpView.serFullNameError(singUpView.getContext().getString(R.string.empty_full_name));
            error = true;

        } else if (TextUtils.isEmpty(email)) {
            singUpView.setEmailError(singUpView.getContext().getString(R.string.empty_email));
            error = true;

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error = true;
            singUpView.setEmailError(singUpView.getContext().getString(R.string.email_incorrect));

        } else if (TextUtils.isEmpty(password)) {
            singUpView.setPasswordError(singUpView.getContext().getString(R.string.empty_password));
            error = true;

        } else if (TextUtils.isEmpty(confirmPassword)) {
            singUpView.setConfirmError(singUpView.getContext().getString(R.string.empty_confirm));
            error = true;

        } else if (!password.equals(confirmPassword)) {
            singUpView.setConfirmError(singUpView.getContext().getString(R.string.password_dont_match));
            error = true;

        } else if (password.length() < 6) {
            singUpView.setPasswordError(singUpView.getContext().getString(R.string.password_short));
            error = true;
        }
        if (!error) {
            final Bitmap imageUser = singUpView.getBitmapIv();
            dataManager.createUser(email, password, fullname, imageUser);
        }
    }

    @Override
    public void setUserPhotoSD(Intent data, ContentResolver context) {
        InputStream inputStream = null;
        try {
            inputStream = context.openInputStream(data.getData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap photo = BitmapFactory.decodeStream(inputStream);

        singUpView.setUserPhoto(scaleBitmap(photo, 500));
    }

    @Override
    public void setUserPhotoCamera(Intent data) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        singUpView.setUserPhoto(scaleBitmap(photo, 500));
    }

    @Override
    public void createUserSuccess() {
        singUpView.navigateToLogin();
    }

    @Override
    public void createUserError(String message) {
        singUpView.showToast(message);
    }

    private Bitmap scaleBitmap(Bitmap bitmapToScale, float maxImageSize) {
        float ratio = Math.min(maxImageSize / bitmapToScale.getWidth(),
                maxImageSize / bitmapToScale.getHeight());
        int width = Math.round(ratio * bitmapToScale.getWidth());
        int height = Math.round(ratio * bitmapToScale.getHeight());

        return Bitmap.createScaledBitmap(bitmapToScale, width, height, true);
    }

    public void getPermissions() {
        if (ContextCompat.checkSelfPermission(singUpView.getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(singUpView.getContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
//             TODO: singUpView.getActivity() - causes OutOfMemory
            if (ActivityCompat.shouldShowRequestPermissionRationale(singUpView.getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//         TODO:   Why it's empty ?
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(singUpView.getActivity(),
                    Manifest.permission.CAMERA)) {
//         TODO:   Why it's empty ?
            } else {
                ActivityCompat.requestPermissions(singUpView.getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(singUpView.getActivity(),
                        new String[]{Manifest.permission.CAMERA}, 2);
            }
        }
    }
}
