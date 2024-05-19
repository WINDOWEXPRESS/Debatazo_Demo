package com.example.debatazo.band;

import java.io.Serializable;

public class BandObject implements Serializable {
    public static final char P = 'P';
    public static final char N = 'N';
    private int id;
    private String description;
    private char type;
    private int num;

    private String error;

    public BandObject(String description, char type){
        this.description = description;
        this.type = type;
        this.error = null;
    }

    public BandObject(String error) {
        this.error = error;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public char getType() {
        return type;
    }

    public int getNum() {
        return num;
    }

    public String getError() {
        return error;
    }
}
