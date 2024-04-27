package com.example.debatazo.token.usuario.iniciarsesion.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.example.debatazo.savesharedpreference.SaveSharedPreference;
import com.example.debatazo.token.usuario.iniciarsesion.data.model.LoggedInUser;
import com.example.debatazo.token.usuario.apirest.RetrofitCliente;
import com.example.debatazo.token.Token;
import com.example.debatazo.token.usuario.md5.SaltMD5Util;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    public void login(String email, String password, Context context , MutableLiveData<Boolean> loadingLiveData, LoginCallBack callBack) {
        try {
            loadingLiveData.setValue(true);
            RetrofitCliente retrofitCliente = RetrofitCliente.getInstancia();
            Call<Token> tokenLlamada = retrofitCliente.getApiUsuario().loginUsuario(email, SaltMD5Util.generateSaltPassword(password));

            tokenLlamada.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    if(response.isSuccessful()){
                        // Se hace el primero pedicion de token con login.

                        loadingLiveData.setValue(false);
                        Token.getInstance().setValueAndUserId(response.body().getValue(),response.body().getUserId());
                        SharedPreferences sharedPreferences = context.getSharedPreferences(SaveSharedPreference.PREFS_TOKEN,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(SaveSharedPreference.USER_ID,Token.getInstance().getUserId());
                        editor.putString(SaveSharedPreference.TOKEN_VALUE,Token.getInstance().getValue());
                        editor.apply();

                        //Con los datos que responde por token se hace pedicion de perfil usario con token obtenido.
                        Call<LoggedInUser> llamada = retrofitCliente.getApiUsuario().getProfile(Token.getInstance().getValue(),Token.getInstance().getUserId());
                        llamada.enqueue(new Callback<LoggedInUser>() {
                            @Override
                            public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
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

                    }else{
                        String errorMessage = "Error al iniciar sesión: " + response.code() + " - " + response.message();
                        callBack.onFailure(new Result.Error(new IOException(errorMessage)));
                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
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

    // Se pasa el context como parametro para hacer borrar de token y sharedPreferences para log out
    public void logout(Context context) {
        Token.removeInstance();
        context.deleteSharedPreferences(SaveSharedPreference.PREFS_TOKEN);
    }
}