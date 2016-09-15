package com.example.roshk1n.foodcalculator.presenters;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.manageres.DataManager;
import com.example.roshk1n.foodcalculator.views.SingUpView;
import com.example.roshk1n.foodcalculator.interfaces.DataSingUpCallback;

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

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(fullname)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            singUpView.showToast(singUpView.getContext().getString(R.string.enter_all_field));
            error = true;

        }   else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                error = true;
                singUpView.showToast(singUpView.getContext().getString(R.string.email_incorrect));

        } else if (!password.equals(confirmPassword)) {
            singUpView.showToast(singUpView.getContext().getString(R.string.password_dont_match));
            error = true;

        } else if(password.length()<6) {
            singUpView.showToast(singUpView.getContext().getString(R.string.password_short));
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

        singUpView.setUserPhoto(scaleBitmap(photo,500,500));
    }

    @Override
    public void setUserPhotoCamera(Intent data) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        singUpView.setUserPhoto(scaleBitmap(photo,500,500));
    }

    @Override
    public void createUserSuccess() {
        singUpView.navigateToLogin();
    }

    @Override
    public void createUserError(String message) {
        singUpView.showToast(message);
    }

    private Bitmap scaleBitmap(Bitmap bitmapToScale, float newWidth, float newHeight) {
        if(bitmapToScale == null)
            return null;
        int width = bitmapToScale.getWidth();
        int height = bitmapToScale.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(newWidth / width, newHeight / height);
        return Bitmap.createBitmap(bitmapToScale, 0, 0, bitmapToScale.getWidth(), bitmapToScale.getHeight(), matrix, true);
    }
}
