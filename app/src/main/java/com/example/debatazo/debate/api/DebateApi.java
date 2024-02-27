package com.example.debatazo.debate.api;

import com.example.debatazo.debate.DebateProducto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DebateApi {
    @GET("debates/list")
    Call<List<DebateProducto>> getAll();

    @POST("debates/create")
    Call<DebateProducto> publicarDebate(@Body DebateProducto debate, @Field("userId") String userId);
}
