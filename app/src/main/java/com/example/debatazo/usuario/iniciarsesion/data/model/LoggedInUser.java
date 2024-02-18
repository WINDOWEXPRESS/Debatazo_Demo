package com.example.debatazo.usuario.iniciarsesion.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String id;
    private String user_name;
    private String full_name;
    private String profile_img;
    private String email;
    private String passwd;
    private String salt;
    private String age;
    private String sex;


    public LoggedInUser(String id, String displayName) {
        this.id = id;
        this.user_name = displayName;
    }

    public String getId() {
        return id;
    }

    public String getUser_name() {
        return user_name;
    }
}