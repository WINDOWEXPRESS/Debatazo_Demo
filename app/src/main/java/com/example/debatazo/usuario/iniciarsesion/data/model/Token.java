package com.example.debatazo.usuario.iniciarsesion.data.model;

public class Token {

    public String value;
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

    public void setValueAndUserId(String value, int userId){
        this.value = value;
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public int getUserId() {
        return userId;
    }

    public static boolean hasInstance(){
        return instance != null;
    }

    public static void removeInstance(){
        instance = null;
    }
}
