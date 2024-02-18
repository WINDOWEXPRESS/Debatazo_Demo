package com.example.debatazo.usuario.iniciarsesion.data;

import com.example.debatazo.usuario.iniciarsesion.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {



            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error al iniciar sesión", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}