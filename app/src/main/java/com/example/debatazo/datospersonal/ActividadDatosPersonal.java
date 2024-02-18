package com.example.debatazo.datospersonal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.debatazo.R;

public class ActividadDatosPersonal extends AppCompatActivity {
    private TextView limiteNumerico;
    private EditText descripcionPersonal;
    private ImageButton volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_datos_personal);

        vincularVista();

        volver.setOnClickListener(view -> {
            finish();
        });

        maximoCaracteres(descripcionPersonal,limiteNumerico);
    }
    private void maximoCaracteres (EditText editText, TextView textView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No necesitas realizar ninguna acción antes de que cambie el texto.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No necesitas realizar ninguna acción mientras cambia el texto.
            }

            @Override
            public void afterTextChanged(Editable editable) {
                textView.setText(getString(R.string.limite_numerico,editable.length()));
            }
        });
    }

    private void vincularVista(){
        limiteNumerico = findViewById(R.id.actividadDP_textV_limiteNumerico);
        descripcionPersonal = findViewById(R.id.actividadDP_editTT_descripcionPersonal);
        volver = findViewById(R.id.actividadDP_imageB_volver);

    }
}