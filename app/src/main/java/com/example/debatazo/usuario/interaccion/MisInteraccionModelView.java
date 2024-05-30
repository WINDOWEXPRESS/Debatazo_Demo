package com.example.debatazo.usuario.interaccion;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.debatazo.debaterecycler.DebateProducto;
import com.example.debatazo.debaterecycler.detalle.objecto.ComentarioObjeto;
import com.example.debatazo.usuario.apirest.RetrofitCliente;
import com.example.debatazo.usuario.iniciarsesion.data.model.Token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class MisInteraccionModelView extends ViewModel {
        public MutableLiveData<List<DebateProducto>> listasDebate;
        private List<DebateProducto> errorDebate = new ArrayList<>();
        private static final double SLEEP_TIME = 300;

        public LiveData<List<DebateProducto>> generaDebateList() {
            if (listasDebate == null) {
                listasDebate = new MutableLiveData<>();
            }
            return listasDebate;
        }

    public void loardListaPorInteracccion(int offset,String type) {
        new Thread(() -> {
            try {
                Thread.sleep((long) ((Math.random() * SLEEP_TIME) + SLEEP_TIME));
                Call<List<DebateProducto>> debateServicio = null;
                switch (type){
                    case ActividadMisInteraccion.KEY_DEBATE_P:
                        debateServicio = RetrofitCliente.getInstancia().getApiUsuario().getDebateCreate(Token.getInstance().getValue(),String.valueOf(offset),Token.getInstance().getUserId());
                        loardDebate(debateServicio);
                        break;
                    case ActividadMisInteraccion.KEY_DEBATE_G:
                        debateServicio = RetrofitCliente.getInstancia().getApiUsuario().getDebateLike(Token.getInstance().getValue(),String.valueOf(offset),Token.getInstance().getUserId());
                        loardDebate(debateServicio);
                        break;
                    case ActividadMisInteraccion.KEY_DEBATE_R:
                        debateServicio = RetrofitCliente.getInstancia().getApiUsuario().getDebateComment(Token.getInstance().getValue(),String.valueOf(offset),Token.getInstance().getUserId());
                        loardDebate(debateServicio);
                        break;
                }
            } catch (InterruptedException e) {
                errorDebate.add(new DebateProducto(e.getMessage()));
                listasDebate.postValue(errorDebate);
            }
        }).start();
    }

    private void loardDebate(Call<List<DebateProducto>> call){
            call.enqueue(new Callback<List<DebateProducto>>() {
                @Override
                public void onResponse(@NonNull Call<List<DebateProducto>> call, @NonNull Response<List<DebateProducto>> response) {
                    if (response.isSuccessful()) {
                        listasDebate.postValue(response.body());
                    } else {
                        onFailure(call, new Throwable(response.errorBody().toString()));
                    }
                }
                @Override
                public void onFailure(@NonNull Call<List<DebateProducto>> call, Throwable t) {
                    if(t instanceof IOException){
                        errorDebate.add(new DebateProducto("Conexion fallida"));
                    }else if(t instanceof HttpException){
                        errorDebate.add(new DebateProducto(t.getMessage()));
                    }else{
                        errorDebate.add(new DebateProducto("error desconocido:" + t.getMessage()));
                    }
                    listasDebate.postValue(errorDebate);
                }
            });
    }
}
