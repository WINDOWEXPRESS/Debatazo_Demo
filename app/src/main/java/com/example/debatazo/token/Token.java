package com.example.debatazo.token;

public class Token {
    private static Token instance;
    private String value;
    private int userId;

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
