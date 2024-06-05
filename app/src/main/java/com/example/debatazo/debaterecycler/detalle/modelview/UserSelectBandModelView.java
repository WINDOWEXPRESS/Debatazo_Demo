package com.example.debatazo.debaterecycler.detalle.modelview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.debatazo.R;
import com.example.debatazo.band.BandObject;
import com.example.debatazo.debaterecycler.DebateProducto;
import com.example.debatazo.debaterecycler.api.servicio.ServicioUserOperations;
import com.example.debatazo.debaterecycler.detalle.objecto.user_operations.UserSelectBandObjecto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class UserSelectBandModelView extends ViewModel {
    public MutableLiveData<List<BandObject>> respuesta;
    private List<BandObject> error = new ArrayList<>();
    private static final double SLEEP_TIME = 50;
    public LiveData<List<BandObject>> getInstance() {
        if (respuesta == null) {
            respuesta = new MutableLiveData<>();
        }
        return respuesta;
    }

    public void selectBand(String token, UserSelectBandObjecto selectBandObject) {
        new Thread(() -> {
            try {
                Thread.sleep((long) ((Math.random() * SLEEP_TIME) + SLEEP_TIME));
                Call<List<BandObject>> call = ServicioUserOperations.getInstance().getRepor().seleccionalBand(token,selectBandObject);
                call.enqueue(new Callback<List<BandObject>>() {
                    @Override
                    public void onResponse(Call<List<BandObject>> call, Response<List<BandObject>> response) {
                        if(response.isSuccessful()){
                            respuesta.postValue(response.body());
                        }else{
                            onFailure(call,new Throwable(response.errorBody().toString()));
                        }
                    }
                    @Override
                    public void onFailure(Call<List<BandObject>> call, Throwable t) {
                        if(t instanceof IOException){
                            error.add(new BandObject(String.valueOf(R.string.conexion_fallido)));
                        }else if(t instanceof HttpException){
                            error.add(new BandObject(t.getMessage()));
                        }else{
                            error.add(new BandObject(String.valueOf(R.string.error_desconocido)));
                            error.add(new BandObject(t.getMessage()));
                        }
                        respuesta.postValue(error);
                    }
                });
            } catch (InterruptedException e) {
                error.add(new BandObject(e.toString()));
                respuesta.postValue(error);
            }
        }).start();
    }

    public void deSelectBand(String token, UserSelectBandObjecto selectBandObject) {
        new Thread(() -> {
            try {
                Thread.sleep((long) ((Math.random() * SLEEP_TIME) + SLEEP_TIME));
                Call<List<BandObject>> call = ServicioUserOperations.getInstance().getRepor().deSeleccionalBand(token,
                        String.valueOf(selectBandObject.getDebate_id()),
                        String.valueOf(selectBandObject.getUser_id()),
                        String.valueOf(selectBandObject.getBand_id()));
                call.enqueue(new Callback<List<BandObject>>() {
                    @Override
                    public void onResponse(Call<List<BandObject>> call, Response<List<BandObject>> response) {
                        if(response.isSuccessful()){
                            respuesta.postValue(response.body());
                        }else{
                            onFailure(call,new Throwable(response.errorBody().toString()));
                        }
                    }
                    @Override
                    public void onFailure(Call<List<BandObject>> call, Throwable t) {
                        error.add(new BandObject(t.toString()));
                        respuesta.postValue(error);
                    }
                });
            } catch (InterruptedException e) {
                error.add(new BandObject(e.toString()));
                respuesta.postValue(error);
            }
        }).start();
    }
}
