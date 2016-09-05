package com.example.roshk1n.foodcalculator.presenters;


import com.example.roshk1n.foodcalculator.views.ResetPasswordView;

/**
 * Created by roshk1n on 9/2/2016.
 */
public interface ResetPasswordPresenter {
    void setView(ResetPasswordView view);

    void resetPassword(String email);
}
