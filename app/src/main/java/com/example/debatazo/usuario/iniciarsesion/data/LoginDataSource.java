package com.example.debatazo.usuario.iniciarsesion.data;

import android.content.Context;
import android.content.SharedPreferences;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;


import com.example.debatazo.utils.GlobalFuntion;
import com.example.debatazo.utils.SharedPreferenceUtils;
import com.example.debatazo.usuario.apirest.RetrofitCliente;
import com.example.debatazo.usuario.iniciarsesion.data.model.LoggedInUser;
import com.example.debatazo.usuario.iniciarsesion.data.model.Token;
import com.example.debatazo.utils.SaltMD5Util;

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
                public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                    if(response.isSuccessful()){
                        // Se hace el primero pedicion de token con login.

                        loadingLiveData.setValue(false);

                        Token.getInstance().setValueAndUserId(response.body().getValue(),response.body().getUserId(), GlobalFuntion.getDateByString(response.body().getExpiration()));
                        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferenceUtils.PREFS_TOKEN,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(SharedPreferenceUtils.USER_ID,Token.getInstance().getUserId());
                        editor.putString(SharedPreferenceUtils.TOKEN_VALUE,Token.getInstance().getValue());
                        editor.putString(SharedPreferenceUtils.EXPIRATION,Token.getInstance().getExpiration());
                        editor.apply();

                        //Con los datos que responde por token se hace pedicion de perfil usario con token obtenido.
                        Call<LoggedInUser> llamada = retrofitCliente.getApiUsuario().getProfile(Token.getInstance().getValue(),Token.getInstance().getUserId());
                        llamada.enqueue(new Callback<LoggedInUser>() {
                            @Override
                            public void onResponse(@NonNull Call<LoggedInUser> call, @NonNull Response<LoggedInUser> response) {
                                if (response.isSuccessful()) {
                                    callBack.onSuccess(new Result.Success<>(response.body()));
                                    loadingLiveData.setValue(false);
                                } else {
                                    String errorMessage = "Error al iniciar sesión 1: " + response.code() + " - " + response.message();
                                    callBack.onFailure(new Result.Error(new IOException(errorMessage)));
                                    loadingLiveData.setValue(false);
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<LoggedInUser> call, @NonNull Throwable t) {
                                loadingLiveData.setValue(false);
                                String errorMessage = "Error al iniciar sesión onFailure: " + t.getMessage();
                                callBack.onFailure(new Result.Error(new IOException(errorMessage)));
                            }
                        });

                    }else{
                        loadingLiveData.setValue(false);
                        String errorMessage = ""+response.code();
                        callBack.onFailure(new Result.Error(new IOException(errorMessage)));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                    loadingLiveData.setValue(false);
                    String errorMessage = "Error al iniciar sesión onFailure: " + t.getMessage();
                    callBack.onFailure(new Result.Error(new IOException(errorMessage)));
                }
            });

        } catch (Exception e) {
            loadingLiveData.setValue(false);
            callBack.onFailure(new Result.Error(new IOException("Error al iniciar sesión 3", e)));
            loadingLiveData.setValue(false);
        }
    }

    // Se pasa el context como parametro para hacer borrar de token y sharedPreferences para log out
    public void logout(Context context) {
        Token.removeInstance();
        context.deleteSharedPreferences(SharedPreferenceUtils.PREFS_TOKEN);
    }
}