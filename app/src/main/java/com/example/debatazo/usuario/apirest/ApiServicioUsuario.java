package com.example.debatazo.usuario.apirest;

import com.example.debatazo.usuario.iniciarsesion.data.model.LoggedInUser;
import com.example.debatazo.usuario.iniciarsesion.data.model.Token;
import com.example.debatazo.usuario.registrar.RegistrarUsuarioPojo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiServicioUsuario {

    //API PARA REGISTRAR
    @POST("users/create")
    Call<ResponseBody> registrarUsuario(@Body RegistrarUsuarioPojo usuarioPojo);

    //API PARA LOGIN
    @POST("users/login")
    Call<Token> loginUsuario(@Query("email") String email, @Query("passwd") String passwd);

    @GET("users/profile/{id}")
    Call<LoggedInUser> getProfile( @Header("token") String token,@Path("id") int id);

    @PUT("users/updateToken")
    Call<String> updateTonk( @Header("token") String token);

}
