package com.example.debatazo.token.usuario.iniciarsesion.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String user_id;
    private String user_name;
    //... other data fields that may be accessible to the UI
    private String full_name;
    private String profile_img;
    private String age;
    private String email;
    private String contrasenia;
    private String sex;


    LoggedInUserView(String displayName) {
        this.user_name = displayName;
    }

    String getUser_name() {
        return user_name;
    }
}