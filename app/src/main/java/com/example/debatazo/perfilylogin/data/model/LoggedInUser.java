package com.example.debatazo.perfilylogin.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String user_id;
    private String user_name;
    private String full_name;
    private String profile_img;
    private String age;
    private String email;
    private String contrasenia;
    private String sex;


    public LoggedInUser(String user_id, String displayName) {
        this.user_id = user_id;
        this.user_name = displayName;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }
}