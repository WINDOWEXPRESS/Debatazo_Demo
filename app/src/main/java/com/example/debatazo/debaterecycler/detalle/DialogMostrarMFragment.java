package com.example.debatazo.debaterecycler.detalle;

import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.debatazo.utils.GlobalConstants;
import com.example.debatazo.utils.GlobalFuntion;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

public class DialogMostrarMFragment extends DialogFragment {
    ImageView desplegableMM_imageB_volver;
    ShapeableImageView desplegableMM_imageC_usuario;
    TextView desplegableMM_textV_nombre,desplegableMM_textV_descripcion;
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
            }else{
                if(value.get(0).getError() == null){
                    if(mostrarMasAdap.getItemCount() > 0){
                        mostrarMasAdap.add(value);
                    }else{
                       mostrarMasAdap.addAll(value);
                    }
                }else{

                }
            }
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

            }
        });

        Picasso.get().load(comentarioObjeto.getProfileImg()).into(desplegableMM_imageC_usuario);
        desplegableMM_textV_nombre.setText(comentarioObjeto.getUserName());
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
                                    && (visibleItemCount + firstVisibleItemPosition) == totalItemCount - GlobalConstants.BASE
                    ){
                        currentTotalItemCount = totalItemCount;
                        listaComentarioModelView.loardChildren(mostrarMasAdap.getItemCount(),debateId,comentarioObjeto.getId());
                    }
                }
            }
        });
        return layout;
    }

    private void enviarMensaje(Button button,EditText editText,EnviarComentarioModelView enviarComentarioMV){
        button.setOnClickListener(view ->{
            GlobalFuntion.validadLogin(getContext());
            if(selectPid == 0){
                pid = comentarioObjeto.getId();
            }else{
                pid = selectPid;
            }
            if(!editText.getText().toString().isEmpty()){
                ComentarioObjeto mesage = new ComentarioObjeto(editText.getText().toString(),pid);
                enviarComentarioMV.enviarComentario(
                        Token.getInstance().getValue(),
                        mesage,
                        debateId,
                        Token.getInstance().getUserId(),
                        (selectBand != null)? String.valueOf(selectBand.getId()) : null
                );
            }else{

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
        desplegableMM_textV_descripcion = layout.findViewById(R.id.desplegableMM_textV_descripcion);
        desplegableMM_recyclerV_comentarios = layout.findViewById(R.id.desplegableMM_recyclerV_comentarios);
        desplegableMM_editT_entrada = layout.findViewById(R.id.desplegableMM_editT_entrada);
        desplegable_bt_enviar = layout.findViewById(R.id.desplegable_bt_enviar);
    }

    private void instanciarMV(){
        enviarComentarioMV = new ViewModelProvider(DialogMostrarMFragment.this).get(EnviarComentarioModelView.class);
        listaComentarioModelView = new ViewModelProvider(DialogMostrarMFragment.this).get(ListaComentarioModelView.class);
    }
}
