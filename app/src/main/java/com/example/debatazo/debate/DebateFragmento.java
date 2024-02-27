package com.example.debatazo.debate;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.debatazo.R;
import com.example.debatazo.debate.detalle.DetalleDebate;
import com.example.debatazo.debate.modelview.DebateProductoModelView;

import java.util.List;


public class DebateFragmento extends Fragment {
    public DebateFragmento(){}
    private List<DebateProducto> debateList;
    private RecyclerView debateRecyclerV;
    private DebateAdaptador debateAdap;
    private Intent intent;
    private ActivityResultLauncher<Intent> resultLauncher;
    public static final String INTENT_KEY = "SDJHSDYFGGEFTEGFUJ";
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragmento_debate,container,false);
        progressBar = layout.findViewById(R.id.fragmentD_progressB);
        debateRecyclerV = layout.findViewById(R.id.fragmentD_recyclerV);

        /*progressBar.setVisibility(View.VISIBLE);
        debateRecyclerV.setVisibility(View.INVISIBLE);*/

        DebateProductoModelView mv = new ViewModelProvider(this).get(DebateProductoModelView.class);
        mv.generaList().observe(getViewLifecycleOwner(),value->{
            debateList = value;
            debateRecyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
            debateAdap = new DebateAdaptador(debateList);
            debateRecyclerV.setAdapter(debateAdap);

            debateAdap.setClickListener((vista,posicion,producto) ->{
                intent = new Intent(getActivity(), DetalleDebate.class);
                intent.putExtra(INTENT_KEY,producto);
                resultLauncher.launch(intent);
            });
            progressBar.setVisibility(View.INVISIBLE);
            debateRecyclerV.setVisibility(View.VISIBLE);
        });

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {});
        return layout;

    }
}