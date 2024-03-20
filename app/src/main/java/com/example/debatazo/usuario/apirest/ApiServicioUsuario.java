package com.example.debatazo.usuario.apirest;

import com.example.debatazo.usuario.iniciarsesion.data.model.LoggedInUser;
import com.example.debatazo.usuario.registrar.RegistrarUsuarioPojo;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiServicioUsuario {

    //API PARA REGISTRAR
    @POST("users/create")
    Call<ResponseBody> registrarUsuario(@Body RegistrarUsuarioPojo usuarioPojo);

    //API PARA LOGIN
    @POST("users/login")
    Call<LoggedInUser> loginUsuario(@Query("email") String email,@Query("passwd") String passwd);
}
