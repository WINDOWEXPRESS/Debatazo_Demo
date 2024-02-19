package com.example.debatazo.usuario.registrar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.debatazo.R;
import com.example.debatazo.databinding.ActividadIniciaSesionBinding;
import com.example.debatazo.databinding.ActividadRegistrarBinding;
import com.google.android.material.textfield.TextInputLayout;
/*TODO HAY FALLO DE LOGICA  CUANDO APARECEN MENSAJES DE ERROR Y
   EN FUTURO PUEDE CAMBIAR POR BONTO ACTIVO  Y HACER LAS COMPROBACIONES CUANDO CLICKEAS*/
public class ActividadRegistrar extends AppCompatActivity {
    private ActividadRegistrarBinding binding;
    private EditText email;
    private EditText contrasenia;
    private EditText contraseniaRepetir;
    private TextView errorMensaje;
    private CheckBox terminosYCondiciones;
    private Button registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActividadRegistrarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vincularVistas();

        RegistrarViewModel registrarViewModel = new RegistrarViewModel();
        registrarViewModel.getRegistrarFormulaEstado().observe(this,registrarFormulaEstado -> {
            if (registrarFormulaEstado == null) {
                return;
            }
            //activar el boton si los datos son validos
            registrar.setEnabled(registrarFormulaEstado.isDataValid());

            //comprobaciones si un campo no es valido imprime el error
            if (registrarFormulaEstado.getEmailError() != null) {
                email.setError(getString(registrarFormulaEstado.getEmailError()));
            }
            if (registrarFormulaEstado.getContraseniaError() != null) {
                contrasenia.setError(getString(registrarFormulaEstado.getContraseniaError()));
            }
            if (registrarFormulaEstado.getContraseniaRepetidoError() != null) {
                contraseniaRepetir.setError(getString(registrarFormulaEstado.getContraseniaRepetidoError()));
            }
            if (registrarFormulaEstado.getContraseniaNoCoincideError() != null){
                contraseniaRepetir.setError(getString(R.string.error_registrar_contrasenia));
            }
        });
        terminosYCondiciones.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            registrarViewModel.RegistrarDataChanged(email.getText().toString(),contrasenia.getText().toString(),contraseniaRepetir.getText().toString(),isChecked);
        });

        TextWatcher afterTextChangedListener = getTextWatcher(registrarViewModel);
        email.addTextChangedListener(afterTextChangedListener);
        contrasenia.addTextChangedListener(afterTextChangedListener);
        contraseniaRepetir.addTextChangedListener(afterTextChangedListener);
        registrarViewModel.getEstadoLiveDataRegistrarExitosa().observe(this,registrarExitosaEstado -> {
            if (registrarExitosaEstado == null) {
                return;
            }
            if (registrarExitosaEstado.booleanValue() == true){
                finish();
            }else {
                errorMensaje.setText(registrarViewModel.getUserRepository().getMensaje());
            }

        });
        registrar.setOnClickListener(view -> {
            registrarViewModel.registrarUsuario(email.getText().toString(),contrasenia.getText().toString());
        });
    }

    @NonNull
    private TextWatcher getTextWatcher(RegistrarViewModel registrarViewModel) {
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                registrarViewModel.RegistrarDataChanged(email.getText().toString(),contrasenia.getText().toString(),contraseniaRepetir.getText().toString(),null);
            }
        };
        return afterTextChangedListener;
    }

    public void vincularVistas(){
        email = binding.actividadRTextILEmail.getEditText();
        contrasenia = binding.actividadRTextILContrasenia.getEditText();
        contraseniaRepetir = binding.actividadRTextILRepetirContrasenia.getEditText();
        errorMensaje = binding.actividadRTextVMensajeError;
        terminosYCondiciones = binding.actividadRCheckBTerminosYCondiciones;
        registrar = binding.actividadRButtonRegistrar;
    }
}