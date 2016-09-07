package com.example.roshk1n.foodcalculator.rest.model.loginApi;

public class LogoutUser {
    private String u_email;
    private String access_token;


    public LogoutUser() {}

    public LogoutUser(String u_email, String access_token) {
        this.u_email = u_email;
        this.access_token = access_token;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
