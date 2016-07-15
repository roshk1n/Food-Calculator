package com.example.roshk1n.foodcalculator.rest.model.loginApi;

/**
 * Created by roshk1n on 7/15/2016.
 */
public class LoginUser {
    private String u_email;
    private String u_password;

    public LoginUser() {}

    public LoginUser(String u_email, String u_password) {
        this.u_email = u_email;
        this.u_password = u_password;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }

    public String getU_password() {
        return u_password;
    }

    public void setU_password(String u_password) {
        this.u_password = u_password;
    }
}
