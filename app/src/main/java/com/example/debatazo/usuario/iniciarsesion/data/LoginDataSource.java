package com.example.debatazo.usuario.iniciarsesion.data;

import com.example.debatazo.usuario.apirest.RetrofitCliente;
import com.example.debatazo.usuario.iniciarsesion.data.model.LoggedInUser;

import java.io.IOException;

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
            // Convertir el email a RequestBody
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), email);

            final LoggedInUser[] user = new LoggedInUser[1];
            new Thread(() -> {
                RetrofitCliente retrofitCliente = RetrofitCliente.getInstancia();
                Call<LoggedInUser>llamada =retrofitCliente.getApiUsuario().obtenerSalUsuario(requestBody);
                llamada.enqueue(new Callback<LoggedInUser>() {
                    @Override
                    public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                        if (response.isSuccessful()) {
                            user[0] = response.body();
                            System.out.println("Soy sal"+user[0].getSalt());
                        } else {
                            onFailure(call,new Throwable("Mensaje error "+response.errorBody()));
                        }
                    }

                    @Override
                    public void onFailure(Call<LoggedInUser> call, Throwable t) {
                        System.out.println("Mensaje error "+t.getCause());
                    }
                });
            }).start();

            /*LoggedInUser fakeUser ;
            if (datoUsuarioObtenido[0].getEmail() == email){
                fakeUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                "Jane Doe");
                return new Result.Success<>(fakeUser);
            }*/
            return new Result.Error(new IOException("Error al iniciar sesión SDasd"));
        } catch (Exception e) {
            return new Result.Error(new IOException("Error al iniciar sesión", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}