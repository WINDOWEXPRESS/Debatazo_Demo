package com.example.debatazo.usuario.iniciarsesion.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Token {

    private String value;
    private int userId;
    private Date expiration;

    //Singleton
    private static Token instance;
    private Token() {}
    public static Token getInstance(){
        if(instance == null){
            instance = new Token();
        }
        return instance;
    }

    public void setValueAndUserId(String value, int userId,Date expiration){
        this.value = value;
        this.userId = userId;
        this.expiration = expiration;
    }

    public String getValue() {
        return value;
    }

    public int getUserId() {
        return userId;
    }

    public String getExpiration() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(expiration);
    }

    public static boolean hasInstance(){
        return instance != null;
    }

    public static void removeInstance(){
        instance = null;
    }
}
