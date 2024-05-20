package com.example.debatazo.debaterecycler.api.servicio;

import static com.example.debatazo.utils.GlobalConstants.BASE_URI_SERVER;
import static com.example.debatazo.utils.GlobalConstants.URL_EMULADOR_XING;

import com.example.debatazo.debaterecycler.api.repo.ComentarioApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicioComentarioProducto {
    private static ServicioComentarioProducto instacia;
    private static ComentarioApi repositorio;

    private ServicioComentarioProducto(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URI_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repositorio = retrofit.create(ComentarioApi.class);
    }
    public static ServicioComentarioProducto getInstance(){
        if(instacia == null){
            instacia = new ServicioComentarioProducto();
        }
        return instacia;
    }

    public ComentarioApi getRepor(){
        if(repositorio == null){
            return null;
        }else{
            return repositorio;
        }
    }
}
