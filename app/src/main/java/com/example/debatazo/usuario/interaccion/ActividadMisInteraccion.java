package com.example.debatazo.usuario.interaccion;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Selection;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.debatazo.R;
import com.example.debatazo.debaterecycler.DebateAdaptador;
import com.example.debatazo.debaterecycler.detalle.DebateDetalle;
import com.example.debatazo.utils.Dialogs;
import com.example.debatazo.utils.GlobalConstants;

public class ActividadMisInteraccion extends AppCompatActivity {
    private ImageButton aMInteraccion_limageB_volver;
    private Bundle bundle;
    private String seleccion;
    private TextView aMInteraccion_textV_debateP,aMInteraccion_textV_debateR,aMInteraccion_textV_debateG,aMInteraccion_textV_carga;
    private TextView aMInteraccui_textV_noElemento;
    private RecyclerView aMInteraccion_recyclerV_contenidos;
    private ProgressBar aMInteraccion_progressB;
    private View.OnClickListener manejador;
    private ActivityResultLauncher<Intent> resultLauncher;
    private DebateAdaptador debateAdaptador;
    private MisInteraccionModelView misInteraccionMV;
    private int currentTotalItemCount = 0;
    public static final String KEY_DEBATE_P = "KEY_DEBATE_P", KEY_DEBATE_R = "KEY_DEBATE_R", KEY_DEBATE_G = "KEY_DEBATE_G";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_mis_interaccion);

        vincularVista();
        vincularModelV();

        bundle = getIntent().getExtras();
        if (bundle == null) {
            setResult(GlobalConstants.ERROR_CODE);
            finish();
        }

        aMInteraccion_recyclerV_contenidos.setLayoutManager(new LinearLayoutManager(ActividadMisInteraccion.this));

        misInteraccionMV.generaDebateList().observe(ActividadMisInteraccion.this, (value)->{
            if(value.size() == 0){
                if(debateAdaptador == null){
                    debateAdaptador = new DebateAdaptador(value);
                    aMInteraccion_recyclerV_contenidos.setAdapter(debateAdaptador);
                    mostrarNoElemento();
                }else{
                    aMInteraccion_textV_carga.setText(getResources().getString(R.string.no_mas_elementos));
                    aMInteraccion_textV_carga.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> {
                        aMInteraccion_textV_carga.setVisibility(View.GONE);
                    }, GlobalConstants.ANIMATION_DURATION);
                }
            }else {
                if (value.get(0).getError() == null) {
                    if (debateAdaptador == null) {
                        debateAdaptador = new DebateAdaptador(value);
                        aMInteraccion_recyclerV_contenidos.setAdapter(debateAdaptador);
                    } else {
                        debateAdaptador.add(value);
                    }
                }else{
                    Dialogs dialogs = new Dialogs(getResources().getString(R.string.error),value.get(0).getError());
                    dialogs.showDialog(ActividadMisInteraccion.this);
                }
            }

            aMInteraccion_progressB.setVisibility(View.GONE);
            aMInteraccion_recyclerV_contenidos.setVisibility(View.VISIBLE);
            activarClick();
            if(debateAdaptador != null){
                debateAdaptador.setClickListener((vista, posicion, producto)->{
                    Intent intent = new Intent(ActividadMisInteraccion.this, DebateDetalle.class);
                    intent.putExtra(GlobalConstants.INTENT_KEY, producto.getDebateId());
                    resultLauncher.launch(intent);
                });
            }
        });

        seleccion = bundle.getString(GlobalConstants.INTENT_KEY);
        if(seleccion.equals(KEY_DEBATE_P)){
            aMInteraccion_textV_debateP.setBackgroundResource(R.drawable.border_bottom);
            animacion_carga();
            cargarLista(seleccion);
        }else if(seleccion.equals(KEY_DEBATE_R)){
            aMInteraccion_textV_debateR.setBackgroundResource(R.drawable.border_bottom);
            animacion_carga();
            cargarLista(seleccion);
        }else if(seleccion.equals(KEY_DEBATE_G)){
            aMInteraccion_textV_debateG.setBackgroundResource(R.drawable.border_bottom);
            animacion_carga();
            cargarLista(seleccion);
        }

        manejador = view -> {
            limbiar_border();
            limbiar_vista();
            currentTotalItemCount = 0;
            if(view.getId() == R.id.aMInteraccion_textV_debateP){
                aMInteraccion_textV_debateP.setBackgroundResource(R.drawable.border_bottom);
                seleccion = KEY_DEBATE_P;
                debateAdaptador = null;
                animacion_carga();
                cargarLista(seleccion);
            }
            if(view.getId() == R.id.aMInteraccion_textV_debateR){
                aMInteraccion_textV_debateR.setBackgroundResource(R.drawable.border_bottom);
                seleccion = KEY_DEBATE_R;
                debateAdaptador = null;
                animacion_carga();
                cargarLista(seleccion);
            }
            if(view.getId() == R.id.aMInteraccion_textV_debateG){
                aMInteraccion_textV_debateG.setBackgroundResource(R.drawable.border_bottom);
                seleccion = KEY_DEBATE_G;
                debateAdaptador = null;
                animacion_carga();
                cargarLista(seleccion);
            }
        };
        aMInteraccion_textV_debateP.setOnClickListener(manejador);
        aMInteraccion_textV_debateR.setOnClickListener(manejador);
        aMInteraccion_textV_debateG.setOnClickListener(manejador);

        aMInteraccion_recyclerV_contenidos.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        aMInteraccion_textV_carga.setText(getResources().getString(R.string.cargando));
                        aMInteraccion_textV_carga.setVisibility(View.VISIBLE);
                        cargarLista(seleccion);
                    }
                }else if(dy < 0 && (visibleItemCount + firstVisibleItemPosition) == totalItemCount){
                    aMInteraccion_textV_carga.setVisibility(View.GONE);
                }
            }
        });

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {});

        aMInteraccion_limageB_volver.setOnClickListener( view ->{
            finish();
        });
    }

    private void vincularVista(){
        aMInteraccion_limageB_volver = findViewById(R.id.aMInteraccion_limageB_volver);
        aMInteraccion_textV_debateP = findViewById(R.id.aMInteraccion_textV_debateP);
        aMInteraccion_textV_debateR = findViewById(R.id.aMInteraccion_textV_debateR);
        aMInteraccion_textV_debateG = findViewById(R.id.aMInteraccion_textV_debateG);
        aMInteraccion_recyclerV_contenidos = findViewById(R.id.aMInteraccion_recyclerV_contenidos);
        aMInteraccion_progressB = findViewById(R.id.aMInteraccion_progressB);
        aMInteraccion_textV_carga = findViewById(R.id.aMInteraccion_textV_carga);
        aMInteraccui_textV_noElemento = findViewById(R.id.aMInteraccui_textV_noElemento);
    }
    private void vincularModelV(){
        misInteraccionMV = new ViewModelProvider(ActividadMisInteraccion.this).get(MisInteraccionModelView.class);
    }
    private void limbiar_border(){
        aMInteraccion_textV_debateP.setBackgroundResource(R.color.trasparente);
        aMInteraccion_textV_debateR.setBackgroundResource(R.color.trasparente);
        aMInteraccion_textV_debateG.setBackgroundResource(R.color.trasparente);
    }

    private void  limbiar_vista(){
        aMInteraccion_recyclerV_contenidos.setVisibility(View.VISIBLE);
        aMInteraccui_textV_noElemento.setVisibility(View.GONE);
    }

    private void animacion_carga(){
        aMInteraccion_progressB.setVisibility(View.VISIBLE);
        aMInteraccion_recyclerV_contenidos.setVisibility(View.GONE);
        descativarClick();
    }
    private void cargarLista(String type){
        int count = (debateAdaptador == null)? 0 : debateAdaptador.getItemCount();
        misInteraccionMV.loardListaPorInteracccion(count,type);
    }

    private void descativarClick(){
        aMInteraccion_textV_debateP.setEnabled(false);
        aMInteraccion_textV_debateR.setEnabled(false);
        aMInteraccion_textV_debateG.setEnabled(false);
    }

    private void activarClick(){
        aMInteraccion_textV_debateP.setEnabled(true);
        aMInteraccion_textV_debateR.setEnabled(true);
        aMInteraccion_textV_debateG.setEnabled(true);
    }
    private void mostrarNoElemento(){
        String mesage = "";
        switch (seleccion){
            case KEY_DEBATE_P:
                mesage = getResources().getString(R.string.todavia_no_has_publicado_ningun_debate);
                break;
            case KEY_DEBATE_R:
                mesage = getResources().getString(R.string.todavia_no_has_comentado_ningun_debate);
                break;
            case KEY_DEBATE_G:
                mesage = getResources().getString(R.string.todavia_no_has_gustado_ningun_debate);
                break;
        }
        aMInteraccui_textV_noElemento.setText(mesage);
        aMInteraccui_textV_noElemento.setVisibility(View.VISIBLE);
        aMInteraccion_recyclerV_contenidos.setVisibility(View.GONE);
    }
}