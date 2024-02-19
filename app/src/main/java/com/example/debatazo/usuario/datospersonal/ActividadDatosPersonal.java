package com.example.debatazo.usuario.datospersonal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.debatazo.R;
import com.example.debatazo.databinding.ActividadDatosPersonalBinding;

public class ActividadDatosPersonal extends AppCompatActivity {
    private ActividadDatosPersonalBinding binding;
    private TextView limiteNumerico;
    private TextView id;
    private TextView nombreUsuario;
    private TextView nombrePersonal;
    private TextView edad;
    private TextView sexo;
    private ImageView perfil;
    private EditText descripcionPersonal;
    private ImageButton volver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActividadDatosPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vincularVista();

        volver.setOnClickListener(view -> {
            finish();
        });

        maximoCaracteres(descripcionPersonal, limiteNumerico);
    }

    private void maximoCaracteres(EditText editText, TextView textView) {
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
                textView.setText(getString(R.string.limite_numerico, editable.length()));
            }
        });

    }

    private void vincularVista() {
        limiteNumerico = binding.actividadDPTextVLimiteNumerico;
        descripcionPersonal = binding.actividadDPEditTTDescripcionPersonal;
        volver = binding.actividadDPImageBVolver;
        id = binding.actividadDPTextVId;
        nombreUsuario = binding.actividadDPTextVNombreUsuario;
        nombrePersonal = binding.actividadDPTextVNombrePersonal;
        edad = binding.actividadDPTextVEdad;

        sexo = binding.actividadDPTextVSexo;
        perfil = binding.actividadDPImageVPerfil;

    }
}