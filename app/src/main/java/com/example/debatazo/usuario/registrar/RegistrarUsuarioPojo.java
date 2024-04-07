package com.example.debatazo.usuario.registrar;

import com.google.gson.annotations.SerializedName;

public class RegistrarUsuarioPojo {

    private String email;

    private String passwd;



    public RegistrarUsuarioPojo(String email, String passwd) {
        this.email = email;
        this.passwd = passwd;
    }

    // Getters y setters

}
