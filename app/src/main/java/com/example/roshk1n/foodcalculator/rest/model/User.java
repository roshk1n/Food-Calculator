package com.example.roshk1n.foodcalculator.rest.model;

/**
 * Created by roshk1n on 7/14/2016.
 */
public class User {
    private String f_name;
    private String l_name;
    private String u_email;
    private String u_password;

    public User(String f_name, String l_name, String u_email, String u_password) {
        this.f_name = f_name;
        this.l_name = l_name;
        this.u_email = u_email;
        this.u_password = u_password;
    }

    public String getF_name() {

        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
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
