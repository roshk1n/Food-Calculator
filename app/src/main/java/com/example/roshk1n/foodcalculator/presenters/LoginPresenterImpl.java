package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.example.roshk1n.foodcalculator.rest.model.*;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by roshk1n on 7/14/2016.
 */
public class LoginPresenterImpl implements LoginPresenter, LoginPresenter.OnLoginFinishedListener {


    @Override
    public void login(String login, String password, OnLoginFinishedListener listener) {

    }

    @Override
    public void onUsernameError() {

    }

    @Override
    public void onPasswordError() {

    }

    @Override
    public void onSuccess() {

    }
}










/*


 public static void registerUser(String f_namem, String l_name, String u_email, String u_password) {

        com.example.roshk1n.foodcalculator.rest.model.User user = new com.example.roshk1n.foodcalculator.rest.model.User("Oleh", "Roshka", "roshk1n.ua@gmail.com", "132132132");
        RestClient restClient = new RestClient();

        restClient.getLoginApi().registrationUser(user, new Callback<RegistrationResponse>() {
            @Override
            public void success(RegistrationResponse responeRegister, Response response) {

            }
            @Override
            public void failure(RetrofitError error) {

            }
        });
    }*/
