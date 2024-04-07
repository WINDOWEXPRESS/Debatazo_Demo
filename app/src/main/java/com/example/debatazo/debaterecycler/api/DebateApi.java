package com.example.debatazo.debaterecycler.api;


import com.example.debatazo.debaterecycler.DebateProducto;
import com.example.debatazo.debaterecycler.detalle.DebateDetalleObjeto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DebateApi {
    @GET("debates/list")
    Call<List<DebateProducto>> getAll();
    @GET("debates/list/{id}")
    Call<DebateDetalleObjeto> getById(@Path("id") String id);
    @POST("debates/create")
    Call<DebateProducto> publicarDebate(@Body DebateProducto debate, @Field("userId") String userId);
}
