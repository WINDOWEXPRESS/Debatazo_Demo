package com.example.debatazo.debaterecycler;

import com.example.debatazo.band.BandObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class DebateProducto implements Serializable {
    private int userId;
    private int debateId;
    private String profileImg;
    private Date releaseDate;
    private String userName;
    private String title;
    private String description;
    private String imageUrl;
    private Set<BandObject> bands;
    private String imageDeleteHast;
    private int like;
    private int comment;
    private String error;

    public DebateProducto(int userId, int debateId, String profileImg, Date releaseDate, String userName, String title, String description, String imageUrl) {
        this.userId = userId;
        this.debateId = debateId;
        this.profileImg = profileImg;
        this.releaseDate = releaseDate;
        this.userName = userName;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.error = null;
    }

    public DebateProducto(String title, String description, Set<BandObject> bands) {
        this.title = title;
        this.description = description;
        this.bands = bands;
        this.error = null;
    }

    public DebateProducto(String error) {
        this.error = error;
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
        return new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(releaseDate);
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageDeleteHash(String imageDeleteHast) {
        this.imageDeleteHast = imageDeleteHast;
    }

    public int getLike() {
        return like;
    }

    public int getComment() {
        return comment;
    }

    public String getError() {
        return error;
    }
}
