package com.example.debatazo.usuario.apirest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCliente {
    private static RetrofitCliente instancia;
    private static ApiUsuario apiUsuario;

    private RetrofitCliente(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiUsuario = retrofit.create(ApiUsuario.class);
    }

    public static ApiUsuario getApiUsuarioServicio(){
        return apiUsuario;
    }

    //Singleton
    public static RetrofitCliente getInstancia(){
        if(instancia == null){
            instancia =  new RetrofitCliente();
        }
        return instancia;
    }
}
