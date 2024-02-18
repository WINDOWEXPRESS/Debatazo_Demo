package com.example.debatazo.usuario.apirest;

import com.example.debatazo.usuario.registrar.RegistrarUsuarioPojo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiUsuario {

    @POST("/users/create")
    Call<RegistrarUsuarioPojo> registrarUsuario(@Body RegistrarUsuarioPojo usuario);
}
