package com.example.roshk1n.foodcalculator.rest.model.loginApi.response;

/**
 *  Created by Mykhailo Nester on 7/14/16.
 */
public class DataRegistration {

    private String verified;
    private String u_email;

    public DataRegistration() {}

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }
}
