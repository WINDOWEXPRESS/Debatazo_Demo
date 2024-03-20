package com.example.debatazo.usuario.iniciarsesion.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private  String user_id;
    private  String user_name;
    private  String full_name;
    private  String profile_img;
    private  String age;
    private  String self;
    private  String sex;

    public LoggedInUser(String user_id, String user_name, String full_name, String profile_img, String age, String self, String sex) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.full_name = full_name;
        this.profile_img = profile_img;
        this.age = age;
        this.self = self;
        this.sex = sex;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getSelf() {
        return self;
    }
}