package com.example.debatazo.debaterecycler.api.servicio;

import static com.example.debatazo.utils.GlobalConstants.URL_EMULADOR_XING;

import com.example.debatazo.debaterecycler.api.repo.DebateApi;
import com.example.debatazo.utils.GlobalConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicioDebates {
    private static ServicioDebates instacia;
    private static DebateApi repositorio;

    private ServicioDebates(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalConstants.BASE_URI_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repositorio = retrofit.create(DebateApi.class);
    }
    public static ServicioDebates getInstance(){
        if(instacia == null){
            instacia = new ServicioDebates();
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
