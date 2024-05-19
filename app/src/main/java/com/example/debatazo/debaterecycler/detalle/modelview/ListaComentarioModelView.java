package com.example.debatazo.debaterecycler.detalle.modelview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.debatazo.debaterecycler.api.servicio.ServicioComentarioProducto;
import com.example.debatazo.debaterecycler.detalle.objecto.ComentarioObjeto;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaComentarioModelView extends ViewModel {
    public MutableLiveData<List<ComentarioObjeto>> listas;
    private List<ComentarioObjeto> error = new ArrayList<ComentarioObjeto>();

    private static final double SLEEP_TIME = 300;

    public LiveData<List<ComentarioObjeto>> generaList() {
        if (listas == null) {
            listas = new MutableLiveData<>();
        }
        return listas;
    }

    public void loardLista(int offset,int debateId) {
        new Thread(() -> {
            try {
                Thread.sleep((long) ((Math.random() * SLEEP_TIME) + SLEEP_TIME));
                Call<List<ComentarioObjeto>> call = ServicioComentarioProducto.getInstance().getRepor().getComments(String.valueOf(offset),String.valueOf(debateId));
                call.enqueue(new Callback<List<ComentarioObjeto>>() {
                    @Override
                    public void onResponse(Call<List<ComentarioObjeto>> call, Response<List<ComentarioObjeto>> response) {
                        if(response.isSuccessful()){
                            listas.postValue(response.body());
                        }else{
                            onFailure(call,new Throwable(response.errorBody().toString()));
                        }
                    }
                    @Override
                    public void onFailure(Call<List<ComentarioObjeto>> call, Throwable t) {
                        error.add(new ComentarioObjeto(t.getMessage()));
                        listas.postValue(error);
                    }
                });
            } catch (InterruptedException e) {
                error.add(new ComentarioObjeto(e.getMessage()));
                listas.postValue(error);
            }
        }).start();
    }

    public void loardChildren(int offset,int debateId, int id) {
        new Thread(() -> {
            try {
                Thread.sleep((long) ((Math.random() * SLEEP_TIME) + SLEEP_TIME));
                Call<List<ComentarioObjeto>> call = ServicioComentarioProducto.getInstance().getRepor().getCommentChildrens(String.valueOf(offset),String.valueOf(debateId),String.valueOf(id));
                call.enqueue(new Callback<List<ComentarioObjeto>>() {
                    @Override
                    public void onResponse(Call<List<ComentarioObjeto>> call, Response<List<ComentarioObjeto>> response) {
                        if(response.isSuccessful()){
                            listas.postValue(response.body());
                        }else{
                            onFailure(call,new Throwable(response.errorBody().toString()));
                        }
                    }
                    @Override
                    public void onFailure(Call<List<ComentarioObjeto>> call, Throwable t) {
                        error.add(new ComentarioObjeto(t.getMessage()));
                        listas.postValue(error);
                    }
                });
            } catch (InterruptedException e) {
                error.add(new ComentarioObjeto(e.getMessage()));
                listas.postValue(error);
            }
        }).start();
    }
}
