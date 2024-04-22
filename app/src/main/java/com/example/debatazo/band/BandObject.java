package com.example.debatazo.band;

public class BandObject {
    public static final String P = "P";
    public static final String N = "N";
    private int id;
    private String description;
    private String type;
    private int num;

    public BandObject(String description, String type){
        this.description = description;
        this.type = type;
    }
}
