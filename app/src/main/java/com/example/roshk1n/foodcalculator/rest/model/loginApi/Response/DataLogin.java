package com.example.roshk1n.foodcalculator.rest.model.loginApi.response;

/**
 * Created by roshk1n on 7/15/2016.
 */
public class DataLogin {
    private String access_token;
    private String u_email;

    public DataLogin() {}

    public String getAccess_token() {

        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }
}
