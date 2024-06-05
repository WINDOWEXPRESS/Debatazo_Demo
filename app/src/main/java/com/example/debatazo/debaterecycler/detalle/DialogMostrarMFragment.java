package com.example.debatazo.debaterecycler.detalle;

import static android.view.WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewStructure;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.debatazo.R;
import com.example.debatazo.band.BandObject;
import com.example.debatazo.debaterecycler.detalle.modelview.EnviarComentarioModelView;
import com.example.debatazo.debaterecycler.detalle.modelview.ListaComentarioModelView;
import com.example.debatazo.debaterecycler.detalle.objecto.ComentarioObjeto;
import com.example.debatazo.usuario.iniciarsesion.data.model.Token;
import com.example.debatazo.usuario.iniciarsesion.ui.login.IniciaSesion;
import com.example.debatazo.utils.Dialogs;
import com.example.debatazo.utils.GlobalConstants;
import com.example.debatazo.utils.GlobalFuntion;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

public class DialogMostrarMFragment extends DialogFragment {
    ImageView desplegableMM_imageB_volver;
    ShapeableImageView desplegableMM_imageC_usuario;
    TextView desplegableMM_textV_nombre,desplegableMM_textV_descripcion,desplegableMM_textV_fechaP,desplegableMM_textV_carga;
    TextView desplegableMM_textV_fondo;
    ProgressBar desplegableMM_progressB;
    RecyclerView desplegableMM_recyclerV_comentarios;
    EditText desplegableMM_editT_entrada;
    Button desplegable_bt_enviar;
    EnviarComentarioModelView enviarComentarioMV;
    ListaComentarioModelView listaComentarioModelView;
    private MostrarMasAdaptador mostrarMasAdap;
    private ComentarioObjeto comentarioObjeto;
    private BandObject selectBand;
    private int debateId,pid = 0,selectPid = 0,currentTotalItemCount = 0;
    private static final String SERIALIZABLE = "SERIALIZABLE",DEBATE_ID = "DEBATE_ID",BAND_OBJECT = "BAND_OBJECT";

    public static DialogMostrarMFragment Instance(ComentarioObjeto comentarioObjeto, int debateId, BandObject selectBand) {
        DialogMostrarMFragment fragment = new DialogMostrarMFragment();
        Bundle args = new Bundle();
        args.putSerializable(SERIALIZABLE,comentarioObjeto);
        args.putInt(DEBATE_ID, debateId);
        args.putSerializable(BAND_OBJECT,selectBand);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.desplegable_mostrar_mas, container, false);

        if(getArguments() == null){
            dismiss();
        }

        comentarioObjeto = (ComentarioObjeto) getArguments().getSerializable(SERIALIZABLE);
        debateId = getArguments().getInt(DEBATE_ID);
        selectBand = (BandObject) getArguments().getSerializable(BAND_OBJECT);

        vincularComponetes(layout);

        instanciarMV();

        listaComentarioModelView.generaList().observe((LifecycleOwner) DialogMostrarMFragment.this, value ->{
            if(value.size() == 0){
                desplegableMM_textV_carga.setText(getResources().getString(R.string.no_mas_elementos));
                desplegableMM_textV_carga.setVisibility(View.VISIBLE);
                new Handler().postDelayed(()->{
                    desplegableMM_textV_carga.setVisibility(View.GONE);
                },GlobalConstants.ANIMATION_DURATION);
            }else{
                if(value.get(0).getError() == null){
                    if(mostrarMasAdap.getItemCount() > 0){
                        desplegableMM_textV_carga.setVisibility(View.GONE);
                        desplegableMM_textV_fondo.setVisibility(View.GONE);
                        desplegableMM_progressB.setVisibility(View.GONE);
                        mostrarMasAdap.add(value);
                    }else{
                        desplegableMM_textV_carga.setVisibility(View.GONE);
                        desplegableMM_textV_fondo.setVisibility(View.GONE);
                        desplegableMM_progressB.setVisibility(View.GONE);
                       mostrarMasAdap.addAll(value);
                    }
                }else{
                    Dialogs dialogs = new Dialogs(getResources().getString(R.string.error),value.get(0).getError());
                    dialogs.showDialog(getContext());
                }
            }
            mostrarMasAdap.setEnabled(true);
        });

        enviarComentarioMV.getInstance().observe((LifecycleOwner) DialogMostrarMFragment.this, value ->{
            if(value.get(0).equals(GlobalConstants.TRUE)){
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(desplegableMM_editT_entrada.getWindowToken(), 0);
                }
                desplegableMM_editT_entrada.clearFocus();
                desplegableMM_editT_entrada.setText("");
                mostrarMasAdap.clear();
                listaComentarioModelView.loardChildren(0,debateId,comentarioObjeto.getId());
            }else{
                Dialogs dialogs = new Dialogs(getResources().getString(R.string.error),value.get(1));
                dialogs.showDialog(getContext());
            }
            desplegable_bt_enviar.setEnabled(true);
        });

        Picasso.get().load(comentarioObjeto.getProfileImg()).into(desplegableMM_imageC_usuario);
        desplegableMM_textV_nombre.setText(comentarioObjeto.getUserName());
        desplegableMM_textV_nombre.append(GlobalFuntion.usuarioEquipo(getContext(),comentarioObjeto.getType()));
        desplegableMM_textV_fechaP.setText(comentarioObjeto.getReleaseDate());
        desplegableMM_textV_descripcion.setText(comentarioObjeto.getDescription());

        desplegableMM_recyclerV_comentarios.setLayoutManager(new LinearLayoutManager(getContext()));
        mostrarMasAdap = mostrarMasAdap == null? new MostrarMasAdaptador(comentarioObjeto,comentarioObjeto.getChildren(),getContext()) : mostrarMasAdap;
        desplegableMM_recyclerV_comentarios.setAdapter(mostrarMasAdap);

        mostrarMasAdap.setClickListener((vista,posicion,comentario) ->{
            desplegableMM_editT_entrada.requestFocus();
            desplegableMM_editT_entrada.setHint(comentario.getUserName());
            selectPid = comentario.getId();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(desplegableMM_editT_entrada, InputMethodManager.SHOW_IMPLICIT);
        });
        
        enviarMensaje(desplegable_bt_enviar,desplegableMM_editT_entrada,enviarComentarioMV);

        desplegableMM_imageB_volver.setOnClickListener(v ->{
            dismiss();
        });


        desplegableMM_recyclerV_comentarios.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    ){
                        currentTotalItemCount = totalItemCount;
                        desplegableMM_textV_carga.setText(getResources().getString(R.string.cargando));
                        desplegableMM_textV_carga.setVisibility(View.VISIBLE);
                        listaComentarioModelView.loardChildren(mostrarMasAdap.getItemCount(),debateId,comentarioObjeto.getId());
                    }
                }
            }
        });
        return layout;
    }

    private void enviarMensaje(Button button,EditText editText,EnviarComentarioModelView enviarComentarioMV){
        button.setOnClickListener(view ->{
            if(!Token.hasInstance()){
                Intent intent = new Intent(getContext(), IniciaSesion.class);
                Dialogs dialogs = new Dialogs(getResources().getString(R.string.advertencia),String.valueOf(R.string.iniciar_sesision),intent,true,true);
                dialogs.showConfirmDialog(getContext());
            }else {
                if (selectPid == 0) {
                    pid = comentarioObjeto.getId();
                } else {
                    pid = selectPid;
                }
                if (!editText.getText().toString().trim().isEmpty()) {
                    button.setEnabled(false);
                    mostrarMasAdap.setEnabled(false);
                    desplegableMM_textV_fondo.setVisibility(View.VISIBLE);
                    desplegableMM_textV_fondo.bringToFront();
                    desplegableMM_progressB.setVisibility(View.VISIBLE);
                    desplegableMM_progressB.bringToFront();
                    ComentarioObjeto mesage = new ComentarioObjeto(editText.getText().toString(), pid);
                    enviarComentarioMV.enviarComentario(
                            Token.getInstance().getValue(),
                            mesage,
                            debateId,
                            Token.getInstance().getUserId(),
                            (selectBand != null) ? String.valueOf(selectBand.getId()) : null
                    );
                } else {
                    Dialogs dialogs = new Dialogs(getResources().getString(R.string.error),getResources().getString(R.string.introduce_algo));
                    dialogs.showDialog(getContext());
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            WindowManager windowManager = (WindowManager) requireContext().getSystemService(getContext().WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(dm);
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,dm.heightPixels - GlobalConstants.DIALOG_HEIGHT);
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimacion;
            getDialog().getWindow().setGravity(Gravity.BOTTOM);
        }
    }

    private void vincularComponetes(View layout){
        desplegableMM_imageB_volver = layout.findViewById(R.id.desplegableMM_imageB_volver);
        desplegableMM_imageC_usuario = layout.findViewById(R.id.desplegableMM_imageC_usuario);
        desplegableMM_textV_nombre = layout.findViewById(R.id.desplegableMM_textV_nombre);
        desplegableMM_textV_fechaP = layout.findViewById(R.id.desplegableMM_textV_fechaP);
        desplegableMM_textV_descripcion = layout.findViewById(R.id.desplegableMM_textV_descripcion);
        desplegableMM_recyclerV_comentarios = layout.findViewById(R.id.desplegableMM_recyclerV_comentarios);
        desplegableMM_editT_entrada = layout.findViewById(R.id.desplegableMM_editT_entrada);
        desplegable_bt_enviar = layout.findViewById(R.id.desplegable_bt_enviar);
        desplegableMM_textV_carga = layout.findViewById(R.id.desplegableMM_textV_carga);
        desplegableMM_progressB = layout.findViewById(R.id.desplegableMM_progressB);
        desplegableMM_textV_fondo = layout.findViewById(R.id.desplegableMM_textV_fondo);
    }

    private void instanciarMV(){
        enviarComentarioMV = new ViewModelProvider(DialogMostrarMFragment.this).get(EnviarComentarioModelView.class);
        listaComentarioModelView = new ViewModelProvider(DialogMostrarMFragment.this).get(ListaComentarioModelView.class);
    }
}
