package com.example.debatazo.usuario.apirest;

import static com.example.debatazo.utils.GlobalConstants.BASE_URI_SERVER;
import static com.example.debatazo.utils.GlobalConstants.URL_EMULADOR;
import static com.example.debatazo.utils.GlobalConstants.URL_EMULADOR_XING;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCliente {
    private static RetrofitCliente instancia;
    private static ApiServicioUsuario apiServicioUsuario;

    private RetrofitCliente(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_EMULADOR_XING)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiServicioUsuario = retrofit.create(ApiServicioUsuario.class);
    }

    public ApiServicioUsuario getApiUsuario(){
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
