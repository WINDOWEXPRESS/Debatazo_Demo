package com.example.debatazo.usuario.registrar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.debatazo.R;
import com.example.debatazo.utils.BrilloUtils;
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
    TextInputLayout contraseniaTextInputLayout;
    TextInputLayout contraseniaRepetirTextInputLayout;
    private CheckBox terminosYCondiciones;
    private Button registrar;
    private ProgressBar cargando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActividadRegistrarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vincularVistas();

        //ajuste de brillo
        BrilloUtils.getInstancia().brilloAppObserver(this);

        RegistrarViewModel registrarViewModel = new RegistrarViewModel();
        registrarViewModel.getRegistrarFormulaEstado().observe(this, registrarFormulaEstado -> {
            if (registrarFormulaEstado == null) {
                return;
            }
            //activar el boton si los datos son validos
            registrar.setEnabled(registrarFormulaEstado.isDataValid());

            //comprobaciones si un campo no es valido imprime el error
            if (registrarFormulaEstado.getEmailError() != null) {
                email.setError(getString(registrarFormulaEstado.getEmailError()));
                terminosYCondiciones.setChecked(false);
            }
            if (registrarFormulaEstado.getContraseniaError() != null) {
                contrasenia.setError(getString(registrarFormulaEstado.getContraseniaError()));
                terminosYCondiciones.setChecked(false);
            }
            if (registrarFormulaEstado.getContraseniaRepetidoError() != null || registrarFormulaEstado.getContraseniaNoCoincideError() != null) {

                if (registrarFormulaEstado.getContraseniaNoCoincideError() != null) {
                    contraseniaRepetir.setError(getString(R.string.error_registrar_contrasenia));
                } else {
                    contraseniaRepetir.setError(getString(registrarFormulaEstado.getContraseniaRepetidoError()));
                }
                terminosYCondiciones.setChecked(false);
            }

        });
        //Mostrar progressBar mientras carga
        registrarViewModel.getLoadingLiveData().observe(this, loading -> cargando.setVisibility(loading ? View.VISIBLE : View.GONE));

        terminosYCondiciones.setOnCheckedChangeListener((compoundButton, isChecked) ->
                registrarViewModel.RegistrarDataChanged(email.getText().toString(), contrasenia.getText().toString(),
                        contraseniaRepetir.getText().toString(), isChecked));

        TextWatcher afterTextChangedListener = getTextWatcher(registrarViewModel);
        email.addTextChangedListener(afterTextChangedListener);
        contrasenia.addTextChangedListener(afterTextChangedListener);
        contraseniaRepetir.addTextChangedListener(afterTextChangedListener);

        registrar.setOnClickListener(view -> {
            String emailUsuario = email.getText().toString();
            String contraseniaUsuario = contrasenia.getText().toString();
            registrarViewModel.registrarUsuario(emailUsuario, contraseniaUsuario, new Callback<ResponseBody>() {

                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    // Manejar el registro exitoso, por ejemplo, mostrar un mensaje de éxito
                    Toast.makeText(ActividadRegistrar.this, "Usuario registrado con éxito", Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    // Manejar el error durante el registro, por ejemplo, mostrar un mensaje de error
                    Toast.makeText(ActividadRegistrar.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    @NonNull
    private TextWatcher getTextWatcher(RegistrarViewModel registrarViewModel) {
        return new TextWatcher() {
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
                registrarViewModel.RegistrarDataChanged(email.getText().toString(), contrasenia.getText().toString(), contraseniaRepetir.getText().toString(), null);
            }
        };
    }

    public void vincularVistas() {
        email = binding.aRegistrarTextILEmail.getEditText();
        contrasenia = binding.aRegistrarTextILContrasenia.getEditText();
        contraseniaTextInputLayout = binding.aRegistrarTextILContrasenia;
        contraseniaRepetir = binding.aRegistrarTextILRepetirContrasenia.getEditText();
        contraseniaRepetirTextInputLayout = binding.aRegistrarTextILRepetirContrasenia;
        terminosYCondiciones = binding.aRegistrarCheckBTerminosYCondiciones;
        registrar = binding.aRegistrarButtonRegistrar;
        cargando = binding.aRegistrarProgressBCargando;
    }
}