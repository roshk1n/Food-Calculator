package com.example.roshk1n.foodcalculator.interfaces;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User;

public interface UserProfileCallback {
    void loadProfileSuccess(User user);
}
