package com.example.roshk1n.foodcalculator;

public class Session {
    private static Session instance;

    private String email;
    private String fullname;
    private String urlPhoto;

    private Session (){}

    public static Session getInstance() {
        return instance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public static void startSession() {
        if(instance==null) {
            instance = new Session();
        }
    }
    public static void destroy() {
        if(instance !=null) {
            instance = null;
        }
    }
}
