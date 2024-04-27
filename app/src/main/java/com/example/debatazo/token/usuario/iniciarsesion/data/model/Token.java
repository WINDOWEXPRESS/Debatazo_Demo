package com.example.debatazo.usuario.iniciarsesion.data.model;

public class Token {

    public String tokenValue;
    public int userId;

    //Singleton
    private static Token instance;
    private Token() {}
    public static Token getInstance(){
        if(instance == null){
            instance = new Token();
        }
        return instance;
    }

    public void setValueAndUserId(String tokenValue, int userId){
        this.tokenValue = tokenValue;
        this.userId = userId;
    }

    public static boolean hasInstance(){
        return instance != null;
    }

    public static void removeInstance(){
        instance = null;
    }
}
