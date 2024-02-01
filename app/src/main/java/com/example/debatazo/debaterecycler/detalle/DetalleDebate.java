package com.example.debatazo.debaterecycler.detalle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.debatazo.R;
import com.example.debatazo.debaterecycler.DebateFragmento;
import com.example.debatazo.debaterecycler.DebateProducto;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

public class DetalleDebate extends AppCompatActivity {

    ImageButton imageButton;
    ShapeableImageView shapeableIV_usuario;
    TextView textV_nombre,textV_fecha,textV_titulo,textV_contenido;
    ImageView imageV_imagenC;
    Bundle bundle;
    DebateProducto producto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_detalle_debate);

        imageButton = findViewById(R.id.detalleD_imageB_volver);
        shapeableIV_usuario = findViewById(R.id.detalleD_shapeableIV_usuario);
        textV_nombre = findViewById(R.id.detalleD_textV_nombre);
        textV_fecha = findViewById(R.id.detalleD_textV_fecha);
        textV_titulo = findViewById(R.id.detalleD_textV_titulo);
        textV_contenido = findViewById(R.id.detalleD_textV_contenido);
        imageV_imagenC = findViewById(R.id.detalleD_imageV_imagenC);

        bundle = getIntent().getExtras();
        producto = (DebateProducto)bundle.getSerializable(DebateFragmento.INTENT_KEY);

        Picasso.get().load(producto.getImagenUsuario()).into(shapeableIV_usuario);
        Picasso.get().load(producto.getImagenUrl()).into(imageV_imagenC);
        textV_nombre.setText(producto.getNombreUsuario());
        textV_fecha.setText(producto.getFechaPublicacion());
        textV_titulo.setText(producto.getTitulo());
        textV_contenido.setText(producto.getContenido());

        imageButton.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}