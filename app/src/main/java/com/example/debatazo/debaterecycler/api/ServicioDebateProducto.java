package com.example.debatazo.debaterecycler.api;

import static com.example.debatazo.utils.GlobalConstants.BASE_URI_SERVER;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicioDebateProducto {
    private static ServicioDebateProducto instacia;
    private static DebateApi repositorio;

    private ServicioDebateProducto(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URI_SERVER)
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
