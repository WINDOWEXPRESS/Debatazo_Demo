package com.example.debatazo.usuario.registrar;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.debatazo.usuario.apirest.RetrofitCliente;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarUsuarioRepo extends ViewModel {

    // Método para registrar un usuario
        public void registrarUsuario(RegistrarUsuarioPojo usuarioPojo, Callback<ResponseBody> callback, MutableLiveData<Boolean> loadingLiveData) {
            loadingLiveData.setValue(true);
            // Lógica para registrar el usuario en la base de datos o en una API
            // Llamar al callback con el resultado
            RetrofitCliente retrofitCliente = RetrofitCliente.getInstancia();
            Call<ResponseBody> llamada = retrofitCliente.getApiUsuario().registrarUsuario(usuarioPojo);
            llamada.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    loadingLiveData.setValue(false);
                    if (response.isSuccessful()) {
                        callback.onResponse(call,response);
                    } else {
                        try {
                            callback.onFailure(call,new Throwable(response.errorBody().string()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    loadingLiveData.setValue(false);
                    callback.onFailure( call,new Throwable("Error inesperado al registrar."));
                    call.cancel();
                }
            });
        }
    }
