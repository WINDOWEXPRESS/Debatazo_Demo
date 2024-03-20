package com.example.debatazo.usuario.apirest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCliente {
    private static RetrofitCliente instancia;
    private static ApiServicioUsuario apiServicioUsuario;

    private RetrofitCliente(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiServicioUsuario = retrofit.create(ApiServicioUsuario.class);
    }

    public static ApiServicioUsuario getApiUsuario(){
        return apiServicioUsuario;
    }

    //Singleton
    public static RetrofitCliente getInstancia(){
        if(instancia == null){
            instancia =  new RetrofitCliente();
        }
        return instancia;
    }
}
