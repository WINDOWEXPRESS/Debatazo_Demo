package com.example.debatazo.usuario.registrar;

import androidx.lifecycle.ViewModel;

import com.example.debatazo.usuario.apirest.RetrofitCliente;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarUsuarioRepo extends ViewModel {

        // Método para registrar un usuario
        public void registrarUsuario(RegistrarUsuarioPojo usuarioPojo) {
            // Lógica para registrar el usuario en la base de datos o en una API
            // Llamar al callback con el resultado
            RetrofitCliente retrofitCliente = RetrofitCliente.getInstancia();
            Call<RegistrarUsuarioPojo> llamada = retrofitCliente.getApiUsuario().registrarUsuario(usuarioPojo);
            llamada.enqueue(new Callback<RegistrarUsuarioPojo>() {
                @Override
                public void onResponse(Call<RegistrarUsuarioPojo> call, Response<RegistrarUsuarioPojo> response) {
                    if (response.isSuccessful()){
                        RegistrarUsuarioPojo user1 = response.body();
                        System.out.println("Regitrar correctamente "+response.message() + "  "+response.code()+ "   "+response.errorBody() );
                    }else {
                        onFailure(call,new Throwable("Registrar fallido"));
                    }


                }

                @Override
                public void onFailure(Call<RegistrarUsuarioPojo> call, Throwable t) {

                    call.cancel();
                }
            });
        }
    }
