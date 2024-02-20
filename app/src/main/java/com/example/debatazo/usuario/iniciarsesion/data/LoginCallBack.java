package com.example.debatazo.usuario.iniciarsesion.data;

import com.example.debatazo.usuario.iniciarsesion.data.model.LoggedInUser;

public interface LoginCallBack {
    void onSuccess(LoggedInUser user);
    void onFailure(Throwable throwable);
}
