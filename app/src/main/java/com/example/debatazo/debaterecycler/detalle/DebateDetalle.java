package com.example.debatazo.debaterecycler.detalle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.debatazo.R;
import com.example.debatazo.band.BandObject;
import com.example.debatazo.debaterecycler.api.servicio.ServicioDebates;
import com.example.debatazo.debaterecycler.detalle.modelview.EnviarComentarioModelView;
import com.example.debatazo.debaterecycler.detalle.modelview.ListaComentarioModelView;
import com.example.debatazo.debaterecycler.detalle.modelview.UserSelectBandModelView;
import com.example.debatazo.debaterecycler.detalle.objecto.ComentarioObjeto;
import com.example.debatazo.debaterecycler.detalle.objecto.DebateDetalleObjeto;
import com.example.debatazo.debaterecycler.detalle.objecto.user_operations.UserSelectBandObjecto;
import com.example.debatazo.usuario.iniciarsesion.data.model.Token;
import com.example.debatazo.utils.GlobalConstants;
import com.example.debatazo.utils.GlobalFuntion;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DebateDetalle extends AppCompatActivity {

    ImageButton imageButton,aDDebate_imageB_meGusta;
    ShapeableImageView shapeableIV_usuario;
    TextView textV_nombre, textV_fecha, textV_titulo, textV_contenido;
    Button aDDebate_bt_band_favor, aDDebate_bt_band_contra, aDDebate_bt_enviar;
    ImageView imageV_imagenC;
    RecyclerView comentarios;
    EditText aDDebate_editT_entrada;
    Bundle bundle;
    ComentarioAdaptador adaptador;
    ListaComentarioModelView listaComentarioMV;
    EnviarComentarioModelView enviarComentarioMV;
    UserSelectBandModelView userSelectBandMV;
    NestedScrollView nestedScrollView;
    private int debateid, currentTotalItemCount = 0, bandButtonWidth, pid = 0;
    private static final int ANCHO = 50;
    private BandObject selectBand, currentSelectBand, favor, contra;
    private Call<DebateDetalleObjeto> debateDetalleService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_detalle_debate);

        vincularComponentes();
        comentarios.setNestedScrollingEnabled(false);
        bundle = getIntent().getExtras();

        if (bundle == null) {
            setResult(GlobalConstants.ERROR_CODE);
            finish();
        }

        debateid = bundle.getInt(GlobalConstants.INTENT_KEY);

        instaciarMV();

        listaComentarioMV.generaList().observe(DebateDetalle.this, value ->{
            if(value.size() == 0){

            }else{
                if(value.get(0).getError() == null){
                    if(adaptador.getItemCount() == 0){
                        adaptador.addAll(value);
                    }else {
                        adaptador.add(value);
                    }
                }else{

                }
            }
        });

        userSelectBandMV.getInstance().observe(DebateDetalle.this, value ->{
            selectBand = currentSelectBand;
            insertaBands(value,(selectBand!= null)? selectBand.getId() : 0);
            adaptador.clear();
            listaComentarioMV.loardLista(0,debateid);
        });

        enviarComentarioMV.getInstance().observe(DebateDetalle.this,value ->{
            if(value.get(0).equals(GlobalConstants.TRUE)){
                InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(aDDebate_editT_entrada.getWindowToken(), 0);
                }
                aDDebate_editT_entrada.clearFocus();
                aDDebate_editT_entrada.setText("");
                adaptador.clear();
                listaComentarioMV.loardLista(0,debateid);
            }else{
            }
        });

        if(Token.hasInstance()){
            debateDetalleService = ServicioDebates.getInstance().getRepor().getById(String.valueOf(debateid), String.valueOf(Token.getInstance().getUserId()));
        }else{
            debateDetalleService = ServicioDebates.getInstance().getRepor().getById(String.valueOf(debateid),null);
        }

        pedirDatos();

        escucharScroll();

        escucharEditext();

        escucharBands();

        enviarComentario();
    }

    private void vincularComponentes() {
        imageButton = findViewById(R.id.aDDebate_imageB_volver);
        shapeableIV_usuario = findViewById(R.id.aDDebate_shapeableIV_usuario);
        textV_nombre = findViewById(R.id.aDDebate_textV_nombre);
        textV_fecha = findViewById(R.id.aDDebate_textV_fecha);
        textV_titulo = findViewById(R.id.aDDebate_textV_titulo);
        textV_contenido = findViewById(R.id.aDDebate_textV_contenido);
        imageV_imagenC = findViewById(R.id.aDDebate_imageV_imagenC);
        aDDebate_bt_band_favor = findViewById(R.id.aDDebate_bt_band_favor);
        aDDebate_bt_band_contra = findViewById(R.id.aDDebate_bt_band_contra);
        comentarios = findViewById(R.id.aDDebate_comentarios);
        nestedScrollView = findViewById(R.id.aDDebate_NestedSV);
        aDDebate_editT_entrada = findViewById(R.id.aDDebate_editT_entrada);
        aDDebate_imageB_meGusta = findViewById(R.id.aDDebate_imageB_meGusta);
        aDDebate_bt_enviar = findViewById(R.id.aDDebate_bt_enviar);
    }

    private void pedirDatos(){
        debateDetalleService.enqueue(new Callback<DebateDetalleObjeto>() {
            @Override
            public void onResponse(@NonNull Call<DebateDetalleObjeto> call, @NonNull Response<DebateDetalleObjeto> response) {
                if (response.isSuccessful()) {
                    DebateDetalleObjeto detalle = response.body();
                    Picasso.get().load(detalle.getList().getProfileImg()).into(shapeableIV_usuario);
                    if (detalle.getList().getImageUrl() != null) {
                        Picasso.get().load(detalle.getList().getImageUrl()).into(imageV_imagenC);
                    }

                    textV_nombre.setText(detalle.getList().getUserName());
                    textV_fecha.setText(detalle.getList().getReleaseDate());
                    textV_titulo.setText(detalle.getList().getTitle());
                    textV_contenido.setText(detalle.getList().getDescription());
                    cambiarMeGusta(detalle.isHasLike());

                    insertaBands(detalle.getBands(),detalle.getBandSelected());

                    comentarios.setLayoutManager(new LinearLayoutManager(DebateDetalle.this));
                    adaptador = new ComentarioAdaptador(detalle.getComments(),DebateDetalle.this,debateid,selectBand,getSupportFragmentManager());
                    comentarios.setAdapter(adaptador);

                    imageButton.setOnClickListener(view -> {
                        setResult(RESULT_CANCELED);
                        finish();
                    });

                    adaptador.setClickListener((vista, posicion, comentario) -> {
                        aDDebate_editT_entrada.requestFocus();
                        aDDebate_editT_entrada.setHint(comentario.getUserName());
                        pid = comentario.getId();
                        InputMethodManager imm = (InputMethodManager) getSystemService(DebateDetalle.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(aDDebate_editT_entrada, InputMethodManager.SHOW_IMPLICIT);
                    });

                }else{
                    onFailure(call,new Throwable(response.errorBody().toString()));
                }
            }
            @Override
            public void onFailure(@NonNull Call<DebateDetalleObjeto> call, @NonNull Throwable t){
                Toast.makeText(DebateDetalle.this,t.getMessage(),Toast.LENGTH_SHORT);
            }
        });
    }

    private void escucharScroll(){
        nestedScrollView.setOnScrollChangeListener((@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)->{
            int height = nestedScrollView.getHeight();
            int contentHeight = nestedScrollView.getChildAt(0).getHeight();
            if(scrollY > 0) {
                if (currentTotalItemCount != adaptador.getItemCount()
                        && ((scrollY + height) >= (contentHeight - GlobalConstants.NESTED))){
                    currentTotalItemCount = adaptador.getItemCount();
                    listaComentarioMV.loardLista(adaptador.getItemCount(), debateid);
                }
            }
        });
    }

    private void escucharEditext(){
        aDDebate_editT_entrada.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String mesage = aDDebate_editT_entrada.getText().toString();
                if(mesage.length() > 0){
                    aDDebate_bt_enviar.setVisibility(View.VISIBLE);
                    aDDebate_imageB_meGusta.setVisibility(View.GONE);
                }else{
                    aDDebate_bt_enviar.setVisibility(View.GONE);
                    aDDebate_imageB_meGusta.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void instaciarMV(){
        listaComentarioMV = new ViewModelProvider(DebateDetalle.this).get(ListaComentarioModelView.class);
        enviarComentarioMV = new ViewModelProvider(DebateDetalle.this).get(EnviarComentarioModelView.class);
        userSelectBandMV = new ViewModelProvider(DebateDetalle.this).get(UserSelectBandModelView.class);
    }

    private void insertaBands(List<BandObject> bands,int id){
        for(BandObject bandObject : bands){
            if(bandObject.getType() == BandObject.P){
                favor = bandObject;
                aDDebate_bt_band_favor.setText(bandObject.getDescription());
            }else if (bandObject.getType() == BandObject.N) {
                contra = bandObject;
                aDDebate_bt_band_contra.setText(bandObject.getDescription());
            }
            if(bandObject.getId() == id){
                selectBand = bandObject;
            }
        }
        bandButtonWidth = aDDebate_bt_band_favor.getWidth();
        construirBand();
    }

    private void construirBand(){
        String message;
        if(selectBand != null){
            if(selectBand.getDescription().equals(aDDebate_bt_band_favor.getText().toString())){
                aDDebate_bt_band_contra.setEnabled(false);
                aDDebate_bt_band_favor.setEnabled(true);
                cambiarBandAncho();
            }else if(selectBand.getDescription().equals(aDDebate_bt_band_contra.getText().toString())){
                aDDebate_bt_band_favor.setEnabled(false);
                aDDebate_bt_band_contra.setEnabled(true);
                cambiarBandAncho();
            }
            message =aDDebate_bt_band_favor.getText().toString()  +" "+ String.valueOf(favor.getNum());
            aDDebate_bt_band_favor.setText(message);
            message = String.valueOf(contra.getNum()) +" "+ aDDebate_bt_band_contra.getText().toString();
            aDDebate_bt_band_contra.setText(message);
        }else{
            aDDebate_bt_band_contra.setEnabled(true);
            aDDebate_bt_band_favor.setEnabled(true);
            aDDebate_bt_band_favor.setText(favor.getDescription());
            aDDebate_bt_band_contra.setText(contra.getDescription());
            cambiarBandAncho();
        }
    }

    private void cambiarBandAncho(){
        ViewGroup.LayoutParams favorLayoutParams = aDDebate_bt_band_favor.getLayoutParams();
        ViewGroup.LayoutParams contraLayoutParams = aDDebate_bt_band_contra.getLayoutParams();
        if(favor.getNum() > contra.getNum()){
            favorLayoutParams.width =bandButtonWidth + ANCHO;
            contraLayoutParams.width =bandButtonWidth - ANCHO;
        }else if(favor.getNum() < contra.getNum()){
            favorLayoutParams.width = bandButtonWidth - ANCHO;
            contraLayoutParams.width = bandButtonWidth + ANCHO;
        }else if(favor.getNum() == contra.getNum()){
            favorLayoutParams.width = bandButtonWidth;
            contraLayoutParams.width = bandButtonWidth;
        }
        aDDebate_bt_band_favor.setLayoutParams(favorLayoutParams);
        aDDebate_bt_band_contra.setLayoutParams(contraLayoutParams);
    }
    private void escucharBands(){
        View.OnClickListener manejador;
        manejador = (View buton)-> {
            UserSelectBandObjecto userSelectBandObjecto;
             if(buton.getId() == R.id.aDDebate_bt_band_favor){
                 GlobalFuntion.validadLogin(DebateDetalle.this);
                 userSelectBandObjecto = new UserSelectBandObjecto(debateid,Token.getInstance().getUserId(),favor.getId());
                 if(selectBand == null){
                     userSelectBandMV.selectBand(Token.getInstance().getValue(),userSelectBandObjecto);
                     currentSelectBand = favor;
                 }else{
                     userSelectBandMV.deSelectBand(Token.getInstance().getValue(),userSelectBandObjecto);
                     currentSelectBand = null;
                 }

             } else if (buton.getId() == R.id.aDDebate_bt_band_contra){
                 GlobalFuntion.validadLogin(DebateDetalle.this);
                 userSelectBandObjecto = new UserSelectBandObjecto(debateid,Token.getInstance().getUserId(),contra.getId());
                 if(selectBand == null){
                     userSelectBandMV.selectBand(Token.getInstance().getValue(),userSelectBandObjecto);
                     currentSelectBand = contra;
                 }else{
                     userSelectBandMV.deSelectBand(Token.getInstance().getValue(),userSelectBandObjecto);
                     currentSelectBand = null;
                 }
             }
        };

        aDDebate_bt_band_favor.setOnClickListener(manejador);
        aDDebate_bt_band_contra.setOnClickListener(manejador);
    }

    private void enviarComentario(){
        aDDebate_bt_enviar.setOnClickListener(view -> {
            GlobalFuntion.validadLogin(DebateDetalle.this);
            String description = aDDebate_editT_entrada.getText().toString();
            if(!description.isEmpty()){
                ComentarioObjeto comentarioObjeto = new ComentarioObjeto(description,pid);
                enviarComentarioMV.enviarComentario(
                        Token.getInstance().getValue(),
                        comentarioObjeto,debateid,
                        Token.getInstance().getUserId(),
                        (selectBand != null)? String.valueOf(selectBand.getId()) : null
                );
            }else{
            }
        });
    }

    private void escucharMeGusta(){
        aDDebate_imageB_meGusta.setOnClickListener(view ->{
            GlobalFuntion.validadLogin(DebateDetalle.this);

        });
    }
    private void cambiarMeGusta(boolean meGusta){
        if(meGusta){
            aDDebate_imageB_meGusta.setImageResource(R.drawable.me_gusta_corazon);
        }else if(!meGusta){
            aDDebate_imageB_meGusta.setImageResource(R.drawable.no_me_gusta_corazon);
        }
    }

}