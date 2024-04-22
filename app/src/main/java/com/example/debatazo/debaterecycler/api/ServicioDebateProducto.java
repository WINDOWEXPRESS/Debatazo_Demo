package com.example.debatazo.debaterecycler.api;

import com.example.debatazo.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicioDebateProducto {
    private static ServicioDebateProducto instacia;
    private static DebateApi repositorio;

    private String URL_ORDENADOR_CHEN = "http://192.168.1.131:8080/";
    private String URL_EMULADOR_XING = "http://192.168.3.30:8080/";
    private String URL_EMULADOR = "http://10.0.2.2:8080/";

    private ServicioDebateProducto(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_EMULADOR_XING)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repositorio = retrofit.create(DebateApi.class);
    }
    public  static ServicioDebateProducto getInstance(){
        if(instacia == null){
            instacia = new ServicioDebateProducto();
        }
        return instacia;
    }

    public DebateApi getRepor(){
        if(repositorio == null){
            return null;
        }else{
            return repositorio;
        }
    }
}
