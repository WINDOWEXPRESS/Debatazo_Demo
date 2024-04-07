package com.example.debatazo.token;

import android.os.Debug;

import androidx.lifecycle.ViewModelProvider;

import com.example.debatazo.debaterecycler.modelview.DebateProductoModelView;
import com.example.debatazo.usuario.apirest.RetrofitCliente;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Token {
    private static Token instance;
    public String value;
    public int userId;

    private Token() {}

    private Token(String value, int userId) {
        this.value = value;
        this.userId = userId;
    }

    public static Token getInstance(){
        if(instance == null){
            instance = new Token();
        }
        return instance;
    }

    public void setValueAndUserId(String value, int userId){
        this.value = value;
        this.userId = userId;
    }

    public static boolean hasInstance(){
        return instance != null;
    }

    public static void removeInstance(){
        instance = null;
    }
}
