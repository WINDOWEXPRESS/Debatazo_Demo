package com.example.debatazo.debaterecycler.api.repo;


import androidx.annotation.Nullable;

import com.example.debatazo.debaterecycler.DebateProducto;
import com.example.debatazo.debaterecycler.detalle.objecto.DebateDetalleObjeto;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DebateApi {
    @GET("debates/list")
    Call<List<DebateProducto>> getAll(@Header("offset") String offset);
    @GET("debates/list/{id}")
    Call<DebateDetalleObjeto> getById(@Path("id") String id, @Nullable @Query("userId") String userId);
    @POST("debates/create")
    Call<ResponseBody> publicarDebate(@Header("token") String token, @Body DebateProducto debate, @Query("userId") String userId);
}
