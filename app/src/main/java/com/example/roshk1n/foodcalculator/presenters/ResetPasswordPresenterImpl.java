package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.interfaces.ResetPasswordCallback;
import com.example.roshk1n.foodcalculator.manageres.FirebaseManager;
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
            FirebaseManager.getInstance().resetPassword(email, new ResetPasswordCallback() {
                @Override
                public void resetSuccess(String message) {
                    resetView.showSnackBar(message);
                }

                @Override
                public void resetError(String message) {
                    resetView.showSnackBar(message);
                }
            });

        } else {
            resetView.errorEmail();
        }
    }

    @Override
    public void destroy() {
        resetView = null;
    }
}
