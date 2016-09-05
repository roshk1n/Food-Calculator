package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.interfaces.ResetPasswordCallback;
import com.example.roshk1n.foodcalculator.remoteDB.FirebaseHelper;
import com.example.roshk1n.foodcalculator.views.ResetPasswordView;

public class ResetPasswordPresenterImpl implements ResetPasswordPresenter{
    private ResetPasswordView resetView;

    @Override
    public void setView(ResetPasswordView view) {
        resetView = view;
    }

    @Override
    public void resetPassword(String email) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            FirebaseHelper.getInstance().resetPassword(email, new ResetPasswordCallback() {
                @Override
                public void showToast(String message) {
                    resetView.showToast(message);
                }
            });

        } else {
            resetView.errorEmail();
        }
    }
}
