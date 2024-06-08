package com.example.debatazo.imgur;

import android.text.BoringLayout;

import okhttp3.MultipartBody;

public class ImgurObject{
    private data data;
    private int status;
    private Boolean success;

    public ImgurObject.data getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public Boolean getSuccess() {
        return success;
    }

    public class data{
        private String error;
        private String deletehash;
        private String link;

        public String getDeletehash() {
            return deletehash;
        }

        public String getLink() {
            return link;
        }

        public String getError() {
            return error;
        }
    }

}
