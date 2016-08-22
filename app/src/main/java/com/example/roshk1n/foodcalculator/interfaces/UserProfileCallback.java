package com.example.roshk1n.foodcalculator.interfaces;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User;

/**
 * Created by roshk1n on 8/21/2016.
 */
public interface UserProfileCallback {
    void loadProfileSuccess(User user);
}
