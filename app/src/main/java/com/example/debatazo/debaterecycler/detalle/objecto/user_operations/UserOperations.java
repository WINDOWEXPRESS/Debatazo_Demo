package com.example.debatazo.debaterecycler.detalle.objecto.user_operations;

public class UserOperations {
    private int user_id;
    private String error;

    public UserOperations(int user_id) {
        this.user_id = user_id;
        this.error = null;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
