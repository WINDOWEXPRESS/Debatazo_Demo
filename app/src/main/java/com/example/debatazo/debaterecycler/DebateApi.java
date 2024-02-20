package com.example.debatazo.debaterecycler;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DebateApi {
    @GET("debates/list")
    Call<List<DebateProducto>> getAll();
}
