package com.example.roshk1n.foodcalculator;

public interface CallbackLocalManager {
    void LoginRealmSuccess(com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User user);
    void showToast(String text);
}
