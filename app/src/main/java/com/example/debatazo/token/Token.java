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

    private Token(String value, int userId) {
        this.value = value;
        this.userId = userId;
    }

    public static Token getInstance(String value, int userId){
        if(instance == null){
            instance = new Token(value,userId);
        }
        return instance;
    }

    public static boolean hasInstance(){
        return instance != null;
    }
}
