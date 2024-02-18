package com.example.debatazo.usuario.registrar;

import com.google.gson.annotations.SerializedName;

public class RegistrarUsuarioPojo {

    private String email;

    private String passwd;

    private String salt;

    public RegistrarUsuarioPojo(String email, String passwd, String salt) {
        this.email = email;
        this.passwd = passwd;
        this.salt = salt;
    }

    // Getters y setters

    public String getEmail() {
        return email;
    }
}
