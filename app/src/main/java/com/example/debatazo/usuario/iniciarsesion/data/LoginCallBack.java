package com.example.debatazo.usuario.iniciarsesion.data;

import com.example.debatazo.usuario.iniciarsesion.data.model.LoggedInUser;

public interface LoginCallBack {
    Result<LoggedInUser> onSuccess(Result<LoggedInUser> user);
    Result<LoggedInUser> onFailure(Result<LoggedInUser> mensajeError);
}
