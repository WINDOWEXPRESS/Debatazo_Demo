package com.example.debatazo.debaterecycler.api.repo;

import androidx.annotation.Nullable;

import com.example.debatazo.debaterecycler.detalle.objecto.ComentarioObjeto;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ComentarioApi {
    @GET("comment/list/{debate_id}")
    Call<List<ComentarioObjeto>> getComments(@Header("offset") String offset, @Path("debate_id") String debate_id);
    @POST("comment/create")
    Call<ResponseBody> hacerComentario(@Header("token") String token,
                                       @Body ComentarioObjeto comentarioObjeto,
                                       @Query("debateId") String debateId,
                                       @Query("userId") String UserId,
                                       @Nullable @Query("bandId") String bandId);
    @GET("comment/list/{debate_id}/{id}")
    Call<List<ComentarioObjeto>> getCommentChildrens(@Header("offset") String offset, @Path("debate_id") String debate_id,@Path("id") String id);
}
