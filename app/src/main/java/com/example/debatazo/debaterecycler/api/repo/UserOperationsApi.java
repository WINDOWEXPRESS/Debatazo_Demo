package com.example.debatazo.debaterecycler.api.repo;

import com.example.debatazo.band.BandObject;
import com.example.debatazo.debaterecycler.detalle.objecto.user_operations.UserSelectBandObjecto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserOperationsApi {
    @POST("selectBand/create")
    Call<List<BandObject>>  seleccionalBand(@Header("token") String token, @Body UserSelectBandObjecto userSelectBand);

    @DELETE("selectBand/delete/{debateId}/{userId}/{bandId}")
    Call<List<BandObject>> deSeleccionalBand(@Header("token") String token,
                                             @Path("debateId") String debateId,
                                             @Path("userId") String userId,
                                             @Path("bandId") String bandId);

}
