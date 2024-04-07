package com.example.debatazo.debaterecycler.detalle;

public class ComentarioObjeto {
    private int userId;
    private String profileImg;
    private String userName;
    private String description;
    private int pid;

    public ComentarioObjeto(int userId, String profileImg, String userName, String description, int pid) {
        this.userId = userId;
        this.profileImg = profileImg;
        this.userName = userName;
        this.description = description;
        this.pid = pid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
