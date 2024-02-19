package com.example.debatazo.debaterecycler;

import com.example.debatazo.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicioDebateProducto {
    private static ServicioDebateProducto instacia;
    private static RepositorioDebateProducto repositorio;

    private ServicioDebateProducto(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.115.205.105:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repositorio = retrofit.create(RepositorioDebateProducto.class);
    }
    public  static ServicioDebateProducto getInstance(){
        if(instacia == null){
            instacia = new ServicioDebateProducto();
        }
        return instacia;
    }

    public RepositorioDebateProducto getRepor(){
        if(repositorio == null){
            return null;
        }else{
            return repositorio;
        }
    }
}
