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
import android.widget.Toast;

import com.example.debatazo.R;
import com.example.debatazo.databinding.ActividadIniciaSesionBinding;
import com.example.debatazo.databinding.ActividadRegistrarBinding;
import com.google.android.material.textfield.TextInputLayout;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                terminosYCondiciones.setChecked(false);
            }
            if (registrarFormulaEstado.getContraseniaRepetidoError() != null) {
                contraseniaRepetir.setError(getString(registrarFormulaEstado.getContraseniaRepetidoError()));
                terminosYCondiciones.setChecked(false);
            }
            if (registrarFormulaEstado.getContraseniaNoCoincideError() != null){
                contraseniaRepetir.setError(getString(R.string.error_registrar_contrasenia));
                terminosYCondiciones.setChecked(false);
            }

        });
        terminosYCondiciones.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            registrarViewModel.RegistrarDataChanged(email.getText().toString(),contrasenia.getText().toString(),contraseniaRepetir.getText().toString(),isChecked);
        });

        TextWatcher afterTextChangedListener = getTextWatcher(registrarViewModel);
        email.addTextChangedListener(afterTextChangedListener);
        contrasenia.addTextChangedListener(afterTextChangedListener);
        contraseniaRepetir.addTextChangedListener(afterTextChangedListener);

        registrar.setOnClickListener(view -> {
            String emailUsuario = email.getText().toString();
            String contraseniaUsuario = contrasenia.getText().toString();
            registrarViewModel.registrarUsuario(emailUsuario, contraseniaUsuario, new Callback<ResponseBody>(){

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // Manejar el registro exitoso, por ejemplo, mostrar un mensaje de éxito
                    Toast.makeText(ActividadRegistrar.this, "Usuario registrado con éxito", Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Manejar el error durante el registro, por ejemplo, mostrar un mensaje de error
                    Toast.makeText(ActividadRegistrar.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                    errorMensaje.setText("Error: " +t.getMessage().toString());
                }
            });
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