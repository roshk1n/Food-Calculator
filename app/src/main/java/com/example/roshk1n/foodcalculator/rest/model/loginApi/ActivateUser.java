package com.example.roshk1n.foodcalculator.rest.model.loginApi;

public class ActivateUser {
    private String v_code;
    private String u_email;

    public ActivateUser() {}

    public String getU_email() {
        return u_email;
    }

    public ActivateUser(String v_code, String u_email) {
        this.v_code = v_code;
        this.u_email = u_email;
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
