package com.example.debatazo.debaterecycler;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.debatazo.R;
import com.example.debatazo.debaterecycler.detalle.DetalleDebate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DebateFragmento extends Fragment {
    public DebateFragmento(){}
    private List<DebateProducto> debateList;
    private RecyclerView debateRecyclerV;
    private DebateAdaptador debateAdap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragmento_debate,container,false);
        debateList = generaDebates();
        debateRecyclerV = layout.findViewById(R.id.fragmentD_recyclerV);
        debateRecyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
        debateAdap = new DebateAdaptador(debateList);
        debateRecyclerV.setAdapter(debateAdap);
        debateAdap.setClickListener((vista,posicion,producto) ->{
            Intent intent;
            intent = new Intent(getActivity(), DetalleDebate.class);
            startActivity(intent);
        });
        return layout;

    }
    private  List<DebateProducto> generaDebates(){
        List<DebateProducto> DebateProductos = new ArrayList<DebateProducto>();
        DebateProductos.add(new DebateProducto(1,1,"https://i.imgur.com/c4ujVR1.png", new Date(),
                "juan","Tomate es mas sano que Manzana", "https://i.imgur.com/Y7rr3sW.png"
                ));
        DebateProductos.add(new DebateProducto(1,1,"https://i.imgur.com/rC1asEd.png", new Date(),
                "Ana","Rock es la musica mas mejor del mundo entero!!!", "https://i.imgur.com/Y7rr3sW.png"
        ));
        return DebateProductos;
    }
}