package com.example.debatazo.debaterecycler.api;

import com.example.debatazo.debaterecycler.DebateProducto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DebateApi {
    @GET("debates/list")
    Call<List<DebateProducto>> getAll();
}
