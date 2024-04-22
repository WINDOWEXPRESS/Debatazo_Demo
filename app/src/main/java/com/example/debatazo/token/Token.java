package com.example.debatazo.token;

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
