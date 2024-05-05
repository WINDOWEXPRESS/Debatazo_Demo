package com.example.debatazo.usuario.iniciarsesion.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private  String user_id;
    private  String user_name;
    private  String full_name;
    private  String profile_img;
    private  String birth_date;
    private  String self;
    private  String sex;

    public LoggedInUser(String user_id, String user_name, String full_name, String profile_img, String birth_date, String self, String sex) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.full_name = full_name;
        this.profile_img = profile_img;
        this.birth_date = birth_date;
        this.self = self;
        this.sex = sex;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getBirth_date() {
        return birth_date;
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