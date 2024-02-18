package com.example.debatazo.debaterecycler;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DebateFragmento extends Fragment {
    public DebateFragmento(){}
    private List<DebateProducto> debateList;
    private RecyclerView debateRecyclerV;
    private DebateAdaptador debateAdap;
    private Intent intent;
    private ActivityResultLauncher<Intent> resultLauncher;
    public static final String INTENT_KEY = "SDJHSDYFGGEFTEGFUJ";
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
            intent = new Intent(getActivity(), DetalleDebate.class);
            intent.putExtra(INTENT_KEY,producto);
            resultLauncher.launch(intent);
        });

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {});
        return layout;

    }
    private  List<DebateProducto> generaDebates(){
        List<DebateProducto> DebateProductos = new ArrayList<DebateProducto>();
        DebateProductos.add(new DebateProducto(1,1,"https://i.imgur.com/c4ujVR1.png", new Date(),
                "juan","Tomate es mas sano que Manzana","El tomate es bajo en calorías y grasas, lo que puede ser beneficioso para aquellos que buscan mantener o perder peso. Puedes destacar cómo el consumo de tomates puede ser parte de una dieta equilibrada y baja en calorías", "https://i.imgur.com/Y7rr3sW.png"
                ));
        DebateProductos.add(new DebateProducto(1,1,"https://i.imgur.com/rC1asEd.png", new Date(),
                "Ana","Rock es la musica mejor del mundo entero!!!","El rock ha resistido la prueba del tiempo y sigue siendo popular entre diversas generaciones. Su capacidad para evolucionar y adaptarse a lo largo de los años demuestra su impacto perdurable" ,"https://i.imgur.com/QXom3DV.jpg"
        ));
        return DebateProductos;
    }
}