package com.example.debatazo.usuario.iniciarsesion.data;

import android.content.Context;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.debatazo.usuario.apirest.RetrofitCliente;
import com.example.debatazo.usuario.iniciarsesion.data.model.LoggedInUser;
import com.example.debatazo.usuario.iniciarsesion.data.model.Token;
import com.example.debatazo.utils.SaltMD5Util;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore

    private MutableLiveData<LoggedInUser> loggedInUserMutableLiveData = new MutableLiveData<>();
    public LiveData<LoggedInUser> getLoggedInUserLiveData() {
        return loggedInUserMutableLiveData;
    }

    //Singleton
    private static volatile LoginRepository instance;
    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return loggedInUserMutableLiveData.getValue() != null;
    }

    public void logout(Context context) {
        loggedInUserMutableLiveData.postValue(null);
        dataSource.logout(context);
    }

    public void login(String username, String password, Context context, MutableLiveData<Boolean> loadingLiveData, LoginCallBack callBack) {
        // handle login
        dataSource.login(username, password,context,loadingLiveData, new LoginCallBack() {
            @Override
            public Result<LoggedInUser> onSuccess(Result<LoggedInUser> user) {
                if (user instanceof Result.Success) {
                    loggedInUserMutableLiveData.postValue(((Result.Success<LoggedInUser>) user).getData());
                }
                callBack.onSuccess(user);
                return user;
            }

            @Override
            public Result<LoggedInUser> onFailure(Result<LoggedInUser> mensajeError) {
                callBack.onFailure(mensajeError);
                return null;
            }
        });
    }

    public void updatePerfil(LoggedInUser user, MutableLiveData<Boolean> loadingLiveData, TextView mensajeError) {
        loadingLiveData.setValue(true);
        Call<ResponseBody> updateProfile = RetrofitCliente.getInstancia().getApiUsuario().updateProfile(Token.getInstance().getValue(),
                Token.getInstance().getUserId(), user);
        updateProfile.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loadingLiveData.setValue(false);
                if (response.isSuccessful()) {
                    try {
                        loggedInUserMutableLiveData.postValue(user);
                        mensajeError.setText(response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    String errorMessage = "Error: " + response.code() + " - " + response.message();
                    mensajeError.setText(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loadingLiveData.setValue(false);
                String errorMessage = "Error: " + t.getCause() + " - " + t.getMessage();
                mensajeError.setText(errorMessage);
            }
        });
    }

    public void recuperarPassword(String email, MutableLiveData<Boolean> loadingLiveData, TextView mensajeError) {
        loadingLiveData.setValue(true);
        //Nueva contraseña de 6 caracteres
        String newPassword = SaltMD5Util.generateRandomCadena();
        //Sal generado
        String passwordEncriptado = SaltMD5Util.generateSaltPassword(newPassword);
        Call<ResponseBody> call = RetrofitCliente.getInstancia().getApiUsuario().recoveryPassword(email,newPassword,passwordEncriptado);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loadingLiveData.setValue(false);
                if (response.isSuccessful()) {
                    try {
                        mensajeError.setText(response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    String errorMessage = "Error: " + response.code() + " - " + response.message();
                    mensajeError.setText(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loadingLiveData.setValue(false);
                String errorMessage = "Error: " + t.getCause() + " - " + t.getMessage();
                mensajeError.setText(errorMessage);
            }
        });
    }

    public void cambiarPassword(int id,String password, MutableLiveData<Boolean> loadingLiveData, TextView mensajeError) {
        loadingLiveData.setValue(true);
        Call<ResponseBody> call = RetrofitCliente.getInstancia().getApiUsuario().changePassword(id,SaltMD5Util.generateSaltPassword(password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loadingLiveData.setValue(false);
                if (response.isSuccessful()) {

                        mensajeError.setText("Contraseña cambiado correctamente.");

                } else {

                    if (response.code() == 404){
                        mensajeError.setText("Error usuario no existe.");
                    }else {
                        mensajeError.setText("Error conexión con el servidor.");
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loadingLiveData.setValue(false);
                String errorMessage = "Error: " + t.getCause() + " - " + t.getMessage();
                mensajeError.setText(errorMessage);
            }
        });
    }


}