package com.example.debatazo.debaterecycler;

import com.example.debatazo.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicioDebateProducto {
    private static ServicioDebateProducto instacia;
    private static DebateApi repositorio;

    private ServicioDebateProducto(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
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
