package com.example.debatazo.usuario.registrar;

import androidx.lifecycle.ViewModel;

import com.example.debatazo.usuario.apirest.RetrofitCliente;
import com.example.debatazo.usuario.iniciarsesion.data.model.LoggedInUser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarUsuarioRepo extends ViewModel {
    private boolean esRegistrarExitosa;
    private int codigo;
    private String mensaje;


    public String getMensaje() {
        return mensaje;
    }

    public int getCodigo() {
        return codigo;
    }

    // Método para registrar un usuario
        public void registrarUsuario(RegistrarUsuarioPojo usuarioPojo, Callback<ResponseBody> callback) {

            // Lógica para registrar el usuario en la base de datos o en una API
            // Llamar al callback con el resultado
            RetrofitCliente retrofitCliente = RetrofitCliente.getInstancia();
            Call<ResponseBody> llamada = retrofitCliente.getApiUsuario().registrarUsuario(usuarioPojo);
            llamada.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        callback.onResponse(call,response);
                    } else {
                        try {
                            callback.onFailure(call,new Throwable(response.errorBody().string()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        codigo = response.code();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    esRegistrarExitosa = false;
                    callback.onFailure( call,new Throwable("Error inesperado al registrar."));
                    call.cancel();
                }
            });
        }
    }
