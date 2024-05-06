package com.example.debatazo.debaterecycler.detalle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.debatazo.R;
import com.example.debatazo.debaterecycler.DebateFragmento;
import com.example.debatazo.debaterecycler.api.ServicioDebateProducto;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DebateDetalle extends AppCompatActivity {

    ImageButton imageButton;
    ShapeableImageView shapeableIV_usuario;
    TextView textV_nombre, textV_fecha, textV_titulo, textV_contenido;
    ImageView imageV_imagenC;
    RecyclerView comentarios;
    Bundle bundle;
    ComentarioAdaptador adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_detalle_debate);

        vincularComponentes();

        bundle = getIntent().getExtras();
        int debateid = 0;
        if (bundle != null) {
            debateid = bundle.getInt(DebateFragmento.INTENT_KEY);
        }

        Call<DebateDetalleObjeto> debateDetalleService = ServicioDebateProducto.getInstance().getRepor().getById(String.valueOf(debateid));

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

                    comentarios.setLayoutManager(new LinearLayoutManager(DebateDetalle.this));
                    adaptador = new ComentarioAdaptador(detalle.getComment());
                    comentarios.setAdapter(adaptador);

                    imageButton.setOnClickListener(view -> {
                        setResult(RESULT_CANCELED);
                        finish();
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<DebateDetalleObjeto> call, @NonNull Throwable t) {

            }
        });
    }

    private void vincularComponentes() {
        imageButton = findViewById(R.id.aDDebate_imageB_volver);
        shapeableIV_usuario = findViewById(R.id.aDDebate_shapeableIV_usuario);
        textV_nombre = findViewById(R.id.aDDebate_textV_nombre);
        textV_fecha = findViewById(R.id.aDDebate_textV_fecha);
        textV_titulo = findViewById(R.id.aDDebate_textV_titulo);
        textV_contenido = findViewById(R.id.aDDebate_textV_contenido);
        imageV_imagenC = findViewById(R.id.aDDebate_imageV_imagenC);
        comentarios = findViewById(R.id.aDDebate_comentarios);
    }
}