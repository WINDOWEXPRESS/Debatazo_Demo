package com.example.debatazo.debaterecycler;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.debatazo.R;
import com.example.debatazo.debaterecycler.detalle.DebateDetalle;
import com.example.debatazo.debaterecycler.modelview.DebateProductoModelView;
import com.example.debatazo.utils.Dialogs;
import com.example.debatazo.utils.GlobalConstants;

import java.util.ArrayList;
import java.util.List;


public class DebateFragmento extends Fragment {
    public DebateFragmento() {
    }
    private List<DebateProducto> debateList;
    private RecyclerView debateRecyclerV;
    private DebateAdaptador debateAdap;
    private Intent intent;
    private int currentTotalItemCount = 0;
    private ActivityResultLauncher<Intent> resultLauncher;
    private Dialogs dialogs;

    ProgressBar progressBar;
    TextView fDebate_textV_recarga;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragmento_debate, container, false);
        progressBar = layout.findViewById(R.id.fragmentD_progressB);
        debateRecyclerV = layout.findViewById(R.id.fDebate_recyclerV);
        fDebate_textV_recarga = layout.findViewById(R.id.fDebate_textV_recarga);

        DebateProductoModelView mv = new ViewModelProvider(this).get(DebateProductoModelView.class);

        mv.generaList().observe(getViewLifecycleOwner(), value ->{
            if(value.size() == 0){
                fDebate_textV_recarga.setText(getResources().getString(R.string.no_mas_elementos));
                fDebate_textV_recarga.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> {
                    fDebate_textV_recarga.setVisibility(View.GONE);
                }, GlobalConstants.ANIMATION_DURATION);
            }else{
                if(debateAdap.getItemCount() == 0){
                    progressBar.setVisibility(View.INVISIBLE);
                    debateRecyclerV.setVisibility(View.VISIBLE);
                }
                if(value.get(0).getError() != null){
                    progressBar.setVisibility(View.INVISIBLE);
                    dialogs = new Dialogs(Dialogs.ERROR,value.get(0).getError());
                    dialogs.showDialog(getContext());
                }else{
                    fDebate_textV_recarga.setVisibility(View.GONE);
                    debateAdap.add(value);
                }
            }
        });

        debateRecyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
        debateAdap= debateAdap == null? new DebateAdaptador(new ArrayList<>()) : debateAdap;
        debateRecyclerV.setAdapter(debateAdap);

        debateRecyclerV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();

                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if(dy > 0){
                    if(
                        currentTotalItemCount != totalItemCount
                        && (visibleItemCount + firstVisibleItemPosition) == totalItemCount
                    ) {
                        currentTotalItemCount = totalItemCount;
                        fDebate_textV_recarga.setText(getResources().getString(R.string.cargando));
                        fDebate_textV_recarga.setVisibility(View.VISIBLE);
                        mv.loardLista(debateAdap.getItemCount());
                    }
                }else if(dy < 0 && (visibleItemCount + firstVisibleItemPosition) == totalItemCount){
                    fDebate_textV_recarga.setVisibility(View.GONE);
                }
            }
        });

        debateAdap.setClickListener((vista, posicion, producto) -> {
            intent = new Intent(getActivity(), DebateDetalle.class);
            intent.putExtra(GlobalConstants.INTENT_KEY, producto.getDebateId());
            resultLauncher.launch(intent);
        });


        if(debateAdap.getItemCount() == GlobalConstants.INDEX){
            mv.loardLista(GlobalConstants.INDEX);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            debateRecyclerV.setVisibility(View.VISIBLE);
        }

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {});
        return layout;
    }

}