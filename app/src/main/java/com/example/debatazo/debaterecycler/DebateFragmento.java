package com.example.debatazo.debaterecycler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.debatazo.R;
import com.example.debatazo.debaterecycler.detalle.DetalleDebate;
import com.example.debatazo.perfilylogin.data.Result;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DebateFragmento extends Fragment {
    public DebateFragmento(){}
    private List<DebateProducto> debateList;
    private RecyclerView debateRecyclerV;
    private DebateAdaptador debateAdap;
    private Intent intent;
    private ActivityResultLauncher<Intent> resultLauncher;
    public static final String INTENT_KEY = "SDJHSDYFGGEFTEGFUJ";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragmento_debate,container,false);

        ServicioDebateProducto servicioDebateProducto = ServicioDebateProducto.getInstance();
        Call<List<DebateProducto>> debateProductoCall = servicioDebateProducto.getRepor().getAll();
        debateProductoCall.enqueue(new Callback<List<DebateProducto>>() {
            @Override
            public void onResponse(Call<List<DebateProducto>> call, Response<List<DebateProducto>> response) {
                debateList = new ArrayList<>(response.body());
            }
            @Override
            public void onFailure(Call<List<DebateProducto>> call, Throwable t) {
            }
        });
        debateRecyclerV = layout.findViewById(R.id.fragmentD_recyclerV);
        debateRecyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
        debateAdap = new DebateAdaptador(debateList);
        debateRecyclerV.setAdapter(debateAdap);

        debateAdap.setClickListener((vista,posicion,producto) ->{
            intent = new Intent(getActivity(), DetalleDebate.class);
            intent.putExtra(INTENT_KEY,producto);
            resultLauncher.launch(intent);
        });

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {});
        return layout;

    }
    /*private  List<DebateProducto> getDebateList(){
        ServicioDebateProducto servicioDebateProducto = ServicioDebateProducto.getInstance();
        Call<List<DebateProducto>> debateProductoCall = servicioDebateProducto.getRepor().getAll();
        debateProductoCall.enqueue(new Callback<List<DebateProducto>>() {
            @Override
            public void onResponse(Call<List<DebateProducto>> call, Response<List<DebateProducto>> response) {
                if(response.isSuccessful()){
                    debateList = response.body();
                    System.out.println("????");
                }else {
                    System.out.println("::::::");
                }
            }
            @Override
            public void onFailure(Call<List<DebateProducto>> call, Throwable t) {
                System.out.println("::::::"+t.getMessage());
            }
        });
    }*/
}