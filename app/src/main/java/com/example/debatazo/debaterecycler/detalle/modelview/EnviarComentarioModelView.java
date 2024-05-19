package com.example.debatazo.debaterecycler.detalle.modelview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.debatazo.debaterecycler.api.servicio.ServicioComentarioProducto;
import com.example.debatazo.debaterecycler.detalle.objecto.ComentarioObjeto;
import com.example.debatazo.utils.GlobalConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnviarComentarioModelView extends ViewModel {
    public MutableLiveData<List<String>> comentario;
    private List<String> message = new ArrayList<String>();
    private static final double SLEEP_TIME = 50;
    public LiveData<List<String>> getInstance() {
        if (comentario == null) {
            comentario = new MutableLiveData<>();
        }
        return comentario;
    }

    public void enviarComentario(String token,ComentarioObjeto comentarioObjeto, int debateId, int userId, String bandId) {
        new Thread(() -> {
            try {
                Thread.sleep((long) ((Math.random() * SLEEP_TIME) + SLEEP_TIME));
                Call<ResponseBody> call = ServicioComentarioProducto.getInstance().getRepor().hacerComentario(token,comentarioObjeto,String.valueOf(debateId),String.valueOf(userId),bandId);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            try {
                                message.add(GlobalConstants.TRUE);
                                message.add(response.body().string());
                                comentario.postValue(message);
                            } catch (IOException e) {
                                onFailure(call,new Throwable(response.errorBody().toString()));
                            }
                        }else{
                            onFailure(call,new Throwable(response.errorBody().toString()));
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        message.add(GlobalConstants.FALSE);
                        message.add(t.toString());
                        comentario.postValue(message);
                    }
                });
            } catch (InterruptedException e) {
                message.add(GlobalConstants.FALSE);
                message.add(e.toString());
                comentario.postValue(message);
            }
        }).start();
    }
}
