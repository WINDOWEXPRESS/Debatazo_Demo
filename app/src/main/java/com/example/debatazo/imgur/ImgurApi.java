package com.example.debatazo.imgur;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImgurApi {
        final String CLIENT_ID = "0e98f6e10d7e6e5";
        @Multipart
        @Headers({"Authorization: Client-ID" + CLIENT_ID})
        @POST("/3/upload")
        Call<ImgurObject> uploadImage(
                @Part MultipartBody.Part image,
                @Part("name") RequestBody name
        );


}
