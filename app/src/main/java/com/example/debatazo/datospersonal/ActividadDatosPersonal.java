package com.example.debatazo.datospersonal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.debatazo.R;

public class ActividadDatosPersonal extends AppCompatActivity {
    private TextView limiteNumerico;
    private EditText descripcionPersonal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_datos_personal);

        vincularVista();

        descripcionPersonal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                limiteNumerico.setText(getString(R.string.limite_numerico,editable.length()));

            }
        });
    }

    private void vincularVista(){
        limiteNumerico = findViewById(R.id.actividadDP_textV_limiteNumerico);
        descripcionPersonal = findViewById(R.id.actividadDP_editTT_descripcionPersonal);

    }
}