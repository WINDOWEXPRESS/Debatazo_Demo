package com.example.debatazo.imgur;

import okhttp3.MultipartBody;

public class ImgurObject{
    private data data;
    private int status;
    private String success;

    public ImgurObject.data getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public String getSuccess() {
        return success;
    }

    public class data{
        private String deletehash;
        private String link;

        public String getDeletehash() {
            return deletehash;
        }

        public String getLink() {
            return link;
        }
    }

}
