package com.example.roshk1n.foodcalculator.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by roshk1n on 7/14/2016.
 */
public class ResponeRegister {
    private  Succeeded succeeded;
    private  Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}

class Data
{
    private String verified;
    private String u_email;

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

class Succeeded
{
    private Boolean seccess;
    private String message;

    public Boolean isSeccess() {
        return seccess;
    }

    public void setSeccess(Boolean seccess) {
        this.seccess = seccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}