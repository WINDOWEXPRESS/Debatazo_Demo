package com.example.debatazo.debaterecycler.detalle.objecto;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ComentarioObjeto implements Serializable {
    private int id;
    private int userId;
    private int debateId;
    private String profileImg;
    private String userName;
    private char type;
    private String description;
    private int pid;
    private Date releaseDate;
    private List<ComentarioObjeto> children;
    private String error;

    public ComentarioObjeto(String description, int pid) {
        this.description = description;
        this.pid = pid;
        this.error = null;
    }

    public ComentarioObjeto(String error) {
        this.error = error;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public String getUserName() {
        return userName;
    }

    public char getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getPid() {
        return pid;
    }

    public String getReleaseDate() {return new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(releaseDate);}

    public List<ComentarioObjeto> getChildren() {
        return children;
    }

    public int getDebateId() {
        return debateId;
    }

    public String getError() { return error; }
}
