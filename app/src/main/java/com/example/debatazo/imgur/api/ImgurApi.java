package com.example.debatazo.imgur.api;

import com.example.debatazo.imgur.ImgurObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImgurApi {
        String CLIENT_ID = "c7632b8f539c192";
        @Multipart
        @Headers({"Authorization: Client-ID" + " " + CLIENT_ID})
        @POST("image")
        Call<ImgurObject> uploadImage(
                @Part MultipartBody.Part image,
                @Part("type") RequestBody type,
                @Part("title") RequestBody title,
                @Part("description") RequestBody description
        );
}
