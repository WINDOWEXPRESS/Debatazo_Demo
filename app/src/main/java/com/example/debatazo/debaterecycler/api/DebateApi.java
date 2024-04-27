package com.example.debatazo.debaterecycler.api;


import com.example.debatazo.debaterecycler.DebateProducto;
import com.example.debatazo.debaterecycler.detalle.DebateDetalleObjeto;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DebateApi {
    @GET("debates/list")
    Call<List<DebateProducto>> getAll();
    @GET("debates/list/{id}")
    Call<DebateDetalleObjeto> getById(@Path("id") String id);
    @POST("debates/create")
    Call<ResponseBody> publicarDebate(@Header("token") String token, @Body DebateProducto debate, @Query("userId") String userId);
}
