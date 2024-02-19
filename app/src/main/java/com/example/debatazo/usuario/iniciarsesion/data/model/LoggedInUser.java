package com.example.debatazo.usuario.iniciarsesion.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private  String user_id;
    private  String user_name;
    private  String full_name;
    private  String profile_img;

    private  String salt;
    private  String age;
    private  String self;
    private  String sex;


    public LoggedInUser(String user_id, String displayName) {
        this.user_id = user_id;
        this.user_name = displayName;
    }

    public String getId() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public String getSalt() {
        return salt;
    }

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }
}