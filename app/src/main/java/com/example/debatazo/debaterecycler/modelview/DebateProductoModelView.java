package com.example.debatazo.debaterecycler.modelview;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.debatazo.R;
import com.example.debatazo.debaterecycler.DebateProducto;
import com.example.debatazo.debaterecycler.api.servicio.ServicioDebates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class DebateProductoModelView extends ViewModel {
    public MutableLiveData<List<DebateProducto>> listas;
    private List<DebateProducto> error = new ArrayList<>();

    private static final double SLEEP_TIME = 300;

    public LiveData<List<DebateProducto>> generaList() {
        if (listas == null) {
            listas = new MutableLiveData<>();
        }
        return listas;
    }

    public void loardLista(int offset) {
        new Thread(() -> {
            try {
                Thread.sleep((long) ((Math.random() * SLEEP_TIME) + SLEEP_TIME));
                Call<List<DebateProducto>> debateServicio = ServicioDebates.getInstance().getRepor().getAll(String.valueOf(offset));
                debateServicio.enqueue(new Callback<List<DebateProducto>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<DebateProducto>> call, @NonNull Response<List<DebateProducto>> response) {
                        if (response.isSuccessful()) {
                            listas.postValue(response.body());
                        } else {
                            onFailure(call, new Throwable(response.errorBody().toString()));
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<List<DebateProducto>> call, Throwable t) {
                        if(t instanceof IOException){
                            error.add(new DebateProducto("Conexion fallida"));
                        }else if(t instanceof HttpException){
                            error.add(new DebateProducto(t.getMessage()));
                        }else{
                            error.add(new DebateProducto("error desconocido:" + t.getMessage()));
                        }
                        listas.postValue(error);
                    }
                });
            } catch (InterruptedException e) {
                error.add(new DebateProducto(e.getMessage()));
                listas.postValue(error);
            }
        }).start();
    }
}
