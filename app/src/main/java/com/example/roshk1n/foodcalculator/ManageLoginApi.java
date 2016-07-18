package com.example.roshk1n.foodcalculator;

import android.util.Log;

import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.ActivateUser;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.LoginUser;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.LogoutUser;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.VerifyUser;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.response.ActivateResponse;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.response.LoginResponse;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.response.LogoutResponse;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.response.RegistrationResponse;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.RegistrationUser;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.response.VerifyResponse;
import com.example.roshk1n.foodcalculator.rest.service.LoginApi;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by roshk1n on 7/10/2016.
 */
public  class ManageLoginApi {

    private static RestClient restClient;

    public ManageLoginApi() {}

    public static void registerUser(String f_namem, String l_name, String u_email, String u_password) {
        RegistrationUser registrationUser = new RegistrationUser(f_namem,l_name,u_email,u_password);

        restClient.getLoginApi().registrationUser(registrationUser, new Callback<RegistrationResponse>() {
            @Override
            public void success(RegistrationResponse registrationResponse, Response response) {
                registrationResponse.getSucceeded();

                registrationResponse.getDataRegistration();


                Log.d("TAG", "success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("TAG", "fail");
            }
        });
    }
    public static void verifyUser(String email) {
        VerifyUser verifyUser = new VerifyUser(email);

        restClient.getLoginApi().verifyUser(verifyUser, new Callback<VerifyResponse>() {
            @Override
            public void success(VerifyResponse verifyResponse, Response response) {
                verifyResponse.getData();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static void activationUser(String code,String email) {
        ActivateUser activateUser = new ActivateUser(code,email);

        restClient.getLoginApi().activationUser(activateUser, new Callback<ActivateResponse>() {
            @Override
            public void success(ActivateResponse activateResponse, Response response) {
                activateResponse.getData();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static void login(String email,String password) {
        LoginUser loginUser = new LoginUser(email,password);

        restClient.getLoginApi().login(loginUser, new Callback<LoginResponse>() {
            @Override
            public void success(LoginResponse loginResponse, Response response) {
                loginResponse.getData();
            }
            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static void logout(String email,String access_token) {
        LogoutUser logoutUser = new LogoutUser(email,access_token);

        restClient.getLoginApi().logout(logoutUser, new Callback<LogoutResponse>() {
            @Override
            public void success(LogoutResponse logoutResponse, Response response) {
                logoutResponse.getData();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}