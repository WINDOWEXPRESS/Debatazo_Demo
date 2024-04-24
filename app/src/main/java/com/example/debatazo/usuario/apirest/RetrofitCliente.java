package com.example.debatazo.usuario.apirest;

import com.example.debatazo.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCliente {
    private static RetrofitCliente instancia;
    private static ApiServicioUsuario apiServicioUsuario;

    private String URL_ORDENADOR_CHEN = "http://192.168.1.149:8080/";
    private String URL_EMULADOR = "http://10.0.2.2:8080/";

    private RetrofitCliente(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_ORDENADOR_CHEN)
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
