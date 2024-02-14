package com.example.debatazo.perfilylogin.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI
    private int id;
    private String email;
    private String contrasenia;
    private String sexo;
    private String nombre;
    private String fechaNacimiento;


    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}