package com.example.debatazo.debaterecycler.api.servicio;

import static com.example.debatazo.utils.GlobalConstants.BASE_URI_SERVER;
import static com.example.debatazo.utils.GlobalConstants.URL_EMULADOR_XING;
import com.example.debatazo.debaterecycler.api.repo.UserOperationsApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicioUserOperations {
    private static ServicioUserOperations instacia;
    private static UserOperationsApi repositorio;

    private ServicioUserOperations(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URI_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repositorio = retrofit.create(UserOperationsApi.class);
    }
    public static ServicioUserOperations getInstance(){
        if(instacia == null){
            instacia = new ServicioUserOperations();
        }
        return instacia;
    }

    public UserOperationsApi getRepor(){
        if(repositorio == null){
            return null;
        }else{
            return repositorio;
        }
    }
}
