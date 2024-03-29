package com.example.debatazo.usuario.iniciarsesion.data.model;

public class Token {
    private  String value;
    private  String userId;

    public Token(String value, String userId) {
        this.value = value;
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public String getUserId() {
        return userId;
    }
}
