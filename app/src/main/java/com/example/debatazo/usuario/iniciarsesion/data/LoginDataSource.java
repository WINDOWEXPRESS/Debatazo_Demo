package com.example.debatazo.usuario.iniciarsesion.data;

import com.example.debatazo.usuario.apirest.RetrofitCliente;
import com.example.debatazo.usuario.iniciarsesion.data.model.LoggedInUser;
import com.example.debatazo.usuario.md5.SaltMD5Util;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String email, String password) {
        try {

            RetrofitCliente retrofitCliente = RetrofitCliente.getInstancia();
            Call<LoggedInUser> llamada = retrofitCliente.getApiUsuario().loginUsuario(email, SaltMD5Util.generateSaltPassword(password));

            final LoggedInUser[] user = new LoggedInUser[1];
            llamada.enqueue(new Callback<LoggedInUser>() {
                @Override
                public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                    if (response.isSuccessful()) {
                        user[0] = response.body();

                    } else {
                        onFailure(call, new Throwable("Mensaje error " + response.errorBody()));

                    }
                }

                @Override
                public void onFailure(Call<LoggedInUser> call, Throwable t) {
                    System.out.println("Mensaje error onFailure " + t.getCause() + " " + t.getMessage());

                }
            });
            return new Result.Success<>(new LoggedInUser("Soy email","Anonimo10222","Pepe Lopez","https://i.imgur.com/zWlUBeC.png","20","VAGOOO","HOMBRE"));
        } catch (Exception e) {
            return new Result.Error(new IOException("Error al iniciar sesión", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}