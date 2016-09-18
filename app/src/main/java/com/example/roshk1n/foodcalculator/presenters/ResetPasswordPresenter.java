package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.views.ResetPasswordView;

public interface ResetPasswordPresenter {
    void setView(ResetPasswordView view);

    void resetPassword(String email);

    void destroy();
}
