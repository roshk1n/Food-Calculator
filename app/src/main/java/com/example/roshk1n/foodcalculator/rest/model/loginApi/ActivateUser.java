package com.example.roshk1n.foodcalculator.rest.model.loginApi;

/**
 * Created by roshk1n on 7/15/2016.
 */
public class ActivateUser {
    private String v_code;
    private String u_email;

    public ActivateUser() {}

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }

    public String getV_code() {
        return v_code;
    }

    public void setV_code(String v_code) {
        this.v_code = v_code;
    }
}
