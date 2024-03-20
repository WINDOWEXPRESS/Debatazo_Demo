package com.example.debatazo.debaterecycler;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DebateProducto implements Serializable {
    private int userId;
    private int debateId;
    private String profileImg;
    private Date releaseDate;
    private String userName;
    private String title;
    private String description;
    private String imgUrl;

    public DebateProducto(int userId, int debateId, String profileImg, Date releaseDate, String userName, String title, String description, String imgUrl) {
        this.userId = userId;
        this.debateId = debateId;
        this.profileImg = profileImg;
        this.releaseDate = releaseDate;
        this.userName = userName;
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
    }

    public int getUserId() {
        return userId;
    }

    public int getDebateId() {
        return debateId;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public String getReleaseDate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(releaseDate);
    }

    public String getUserName() {
        return userName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
