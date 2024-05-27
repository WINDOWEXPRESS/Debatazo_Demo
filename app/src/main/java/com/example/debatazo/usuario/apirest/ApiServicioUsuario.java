package com.example.debatazo.usuario.apirest;

import com.example.debatazo.debaterecycler.DebateProducto;
import com.example.debatazo.usuario.iniciarsesion.data.model.LoggedInUser;
import com.example.debatazo.usuario.iniciarsesion.data.model.Token;
import com.example.debatazo.usuario.registrar.RegistrarUsuarioPojo;

import java.util.List;

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
    Call<LoggedInUser> getProfile(@Header("token") String token, @Path("id") int id);

    @PUT("users/profile/update/{id}")
    Call<ResponseBody> updateProfile(@Header("token") String token, @Path("id") int id,@Body LoggedInUser user);

    @PUT("users/passwd/forget/{email}/{passwd}/{passwordEncriptado}")
    Call<ResponseBody> recoveryPassword(@Path("email") String email,@Path("passwd") String passwd,@Path("passwordEncriptado") String passwordEncriptado);

    @PUT("users/passwd/change/{id}/{passwd}")
    Call<ResponseBody> changePassword(@Path("id") int id,@Path("passwd") String passwd);

    @GET("users/profile/{id}/debates")
    Call<List<DebateProducto>> getUserDebateCreate(@Header("token") String token,@Header("offset") String offset, @Path("id") int id);

}
