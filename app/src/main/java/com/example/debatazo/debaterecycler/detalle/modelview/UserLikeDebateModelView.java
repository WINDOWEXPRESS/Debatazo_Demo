package com.example.debatazo.debaterecycler.detalle.modelview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.debatazo.R;
import com.example.debatazo.debaterecycler.DebateProducto;
import com.example.debatazo.debaterecycler.api.servicio.ServicioUserOperations;
import com.example.debatazo.debaterecycler.detalle.objecto.user_operations.UserLikeDebatesObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class UserLikeDebateModelView extends ViewModel {
    public MutableLiveData<String> respuesta;
    private static final double SLEEP_TIME = 50;
    public LiveData<String> getInstance() {
        if (respuesta == null) {
            respuesta = new MutableLiveData<>();
        }
        return respuesta;
    }

    public void likeDebate(String token, UserLikeDebatesObject userLikeDebates) {
        new Thread(() -> {
            try {
                Thread.sleep((long) ((Math.random() * SLEEP_TIME) + SLEEP_TIME));
                Call<ResponseBody> call = ServicioUserOperations.getInstance().getRepor().like(token,userLikeDebates);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            try {
                                respuesta.postValue(response.body().string());
                            } catch (IOException e) {
                                onFailure(call,new Throwable(response.errorBody().toString()));
                            }
                        }else{
                            onFailure(call,new Throwable(response.errorBody().toString()));
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if(t instanceof IOException){
                            respuesta.postValue(String.valueOf(R.string.conexion_fallido).concat("<>"));
                        }else if(t instanceof HttpException){
                            respuesta.postValue(t.getMessage());
                        }else{
                            respuesta.postValue(String.valueOf(R.string.error_desconocido).concat("<>").concat(t.getMessage()));
                        }
                    }
                });
            } catch (InterruptedException e) {
                respuesta.postValue(e.toString());
            }
        }).start();
    }

    public void desLikeDebate(String token,UserLikeDebatesObject userLikeDebates) {
        new Thread(() -> {
            try {
                Thread.sleep((long) ((Math.random() * SLEEP_TIME) + SLEEP_TIME));
                Call<ResponseBody> call = ServicioUserOperations.getInstance().getRepor().noLike(
                        token,
                        String.valueOf(userLikeDebates.getDebate_id()),
                        String.valueOf(userLikeDebates.getUser_id()));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            try {
                                respuesta.postValue(response.body().string());
                            } catch (IOException e) {
                                onFailure(call, new Throwable(e.getMessage()));
                            }
                        }else{
                            onFailure(call,new Throwable(response.errorBody().toString()));
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        respuesta.postValue(t.getMessage());
                    }
                });
            } catch (InterruptedException e) {
                respuesta.postValue(e.getMessage());
            }
        }).start();
    }
}
