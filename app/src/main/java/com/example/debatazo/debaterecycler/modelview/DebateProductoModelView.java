package com.example.debatazo.debaterecycler.modelview;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.debatazo.debaterecycler.DebateProducto;
import com.example.debatazo.debaterecycler.api.ServicioDebateProducto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DebateProductoModelView extends ViewModel {
    public MutableLiveData<List<DebateProducto>> listas;

    private static final double SLEEP_TIME = 100;

    public LiveData<List<DebateProducto>> generaList() {
        if (listas == null) {
            listas = new MutableLiveData<>();
            loardLista();
        }
        return listas;
    }

    public void loardLista() {
        new Thread(() -> {
            try {
                Thread.sleep((long) ((Math.random() * SLEEP_TIME) + SLEEP_TIME));
                Call<List<DebateProducto>> debateServicio = ServicioDebateProducto.getInstance().getRepor().getAll();
                debateServicio.enqueue(new Callback<List<DebateProducto>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<DebateProducto>> call, @NonNull Response<List<DebateProducto>> response) {
                        if (response.isSuccessful()) {
                            listas.postValue(response.body());
                        } else {
                            onFailure(call, new Throwable());
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<DebateProducto>> call, Throwable t) {
                        Log.println(Log.INFO, "Error", String.valueOf(t.getCause()));
                    }
                });
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }
}
