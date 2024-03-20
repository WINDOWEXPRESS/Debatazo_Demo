package com.example.debatazo.usuario.iniciarsesion.data;

import androidx.lifecycle.MutableLiveData;

import com.example.debatazo.usuario.apirest.RetrofitCliente;
import com.example.debatazo.usuario.iniciarsesion.data.model.LoggedInUser;
import com.example.debatazo.usuario.md5.SaltMD5Util;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    public void login(String email, String password, MutableLiveData<Boolean> loadingLiveData, LoginCallBack callBack) {
        try {
            loadingLiveData.setValue(true);
            RetrofitCliente retrofitCliente = RetrofitCliente.getInstancia();
            Call<LoggedInUser> llamada = retrofitCliente.getApiUsuario().loginUsuario(email, SaltMD5Util.generateSaltPassword(password));
            llamada.enqueue(new Callback<LoggedInUser>() {
                @Override
                public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                    loadingLiveData.setValue(false);
                    if (response.isSuccessful()) {
                        callBack.onSuccess(new Result.Success<>(response.body()));
                    } else {
                        String errorMessage = "Error al iniciar sesión: " + response.code() + " - " + response.message();
                        callBack.onFailure(new Result.Error(new IOException(errorMessage)));
                    }
                }

                @Override
                public void onFailure(Call<LoggedInUser> call, Throwable t) {
                    loadingLiveData.setValue(false);
                    String errorMessage = "Error al iniciar sesión onFailure: " + t.getMessage();
                    callBack.onFailure(new Result.Error(new IOException(errorMessage)));
                }
            });
        } catch (Exception e) {
            loadingLiveData.setValue(false);
            callBack.onFailure(new Result.Error(new IOException("Error al iniciar sesión", e)));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}