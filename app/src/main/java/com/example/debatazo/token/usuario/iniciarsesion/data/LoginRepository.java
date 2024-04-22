package com.example.debatazo.token.usuario.iniciarsesion.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.debatazo.token.usuario.iniciarsesion.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore

    private MutableLiveData<LoggedInUser> loggedInUserMutableLiveData = new MutableLiveData<>();
    public LiveData<LoggedInUser> getLoggedInUserLiveData() {
        return loggedInUserMutableLiveData;
    }

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
}