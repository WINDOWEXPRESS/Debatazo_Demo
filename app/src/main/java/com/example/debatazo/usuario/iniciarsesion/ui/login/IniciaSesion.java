package com.example.debatazo.usuario.iniciarsesion.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.debatazo.R;

import com.example.debatazo.utils.BrilloUtils;
import com.example.debatazo.databinding.ActividadIniciaSesionBinding;
import com.example.debatazo.utils.SharedPreferenceUtils;
import com.example.debatazo.usuario.registrar.ActividadRegistrar;
import com.google.android.material.textfield.TextInputLayout;

public class IniciaSesion extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private ActividadIniciaSesionBinding binding;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextInputLayout passwordTextInputLayout;
    private Button loginButton;
    private ProgressBar cargando;
    private ImageView gmail;
    private ImageView wechat;
    private ImageView facebook;
    private TextView olvidarContrasenia;
    private TextView registrar;
    private CheckBox recordar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActividadIniciaSesionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //ajuste de brillo
        BrilloUtils.getInstancia().brilloAppObserver(this);

        // Crear una instancia del ViewModel utilizando un ViewModelProvider y una Factory personalizada
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        vincularVistas();

        // Observa los cambios en el estado del formulario de inicio de sesión
        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            // Verifica si el estado del formulario es nulo
            if (loginFormState == null) {
                return; // Sal de la función si el estado del formulario es nulo
            }
            // Habilita o deshabilita el botón de inicio de sesión según si los datos son válidos
            loginButton.setEnabled(loginFormState.isDataValid());
            // Verifica si hay un error en el nombre de usuario y lo muestra si existe
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));

            }
            // Verifica si hay un error en la contraseña y lo muestra si existe
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
                // Configura el icono final del TextInputLayout según si hay un error en la contraseña
                passwordTextInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
            } else {
                // Configura el icono final del TextInputLayout como un icono de alternar contraseña si no hay errores
                passwordTextInputLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
            }
        });

        loginViewModel.getLoadingLiveData().observe(this, loading -> cargando.setVisibility(loading?View.VISIBLE:View.GONE));

        //OBSERVAR EL RESULTADO DE LOGIN
        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            //Complete and destroy login activity once successful
            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
               //         passwordEditText.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        mostrarInfoConSharedPreference(usernameEditText, passwordEditText, recordar);

        // Establece un listener para capturar el evento cuando se presiona una acción en el teclado virtual
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            // Verifica si la acción es "Done" en el teclado virtual
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Cuando se presiona "Done", se llama al método login del ViewModel
                // para intentar iniciar sesión con el nombre de usuario y la contraseña proporcionados
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),IniciaSesion.this);
            }
            // Devuelve falso para indicar que el evento no está consumido y puede ser manejado por otros listeners
            return false;
        });

        loginButton.setOnClickListener(v -> {
            String email = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceUtils.PREFS_CUENTA, MODE_PRIVATE);
            // Editar los valores de SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //Si esta marcado el recordar se guarda con un sharedPreferences
            if (recordar.isChecked()) {
                // Obtener una instancia de SharedPreferences
                editor.putString(SharedPreferenceUtils.EMAIL, email);
                editor.putString(SharedPreferenceUtils.CONTRASENIA, password);
                editor.putBoolean(SharedPreferenceUtils.RECORDAR, true);
            } else {
                editor.clear();
            }
            editor.apply(); // Guardar los cambios

            loginViewModel.login(email, password,IniciaSesion.this);
        });

        olvidarContrasenia.setOnClickListener(view ->
                Toast.makeText(IniciaSesion.this, "Sin implementar ", Toast.LENGTH_LONG).show()
        );

        gmail.setOnClickListener(view -> otraFormaDeAcceso());
        wechat.setOnClickListener(view -> otraFormaDeAcceso());
        facebook.setOnClickListener(view -> otraFormaDeAcceso());

        registrar.setOnClickListener(view -> {
            Intent i = new Intent(IniciaSesion.this, ActividadRegistrar.class);
            startActivity(i);
        });


    }

    private void vincularVistas() {
        usernameEditText = binding.aISesionTextILEmail.getEditText();
        passwordEditText = binding.aISesionTextILContrasenia.getEditText();
        passwordTextInputLayout = binding.aISesionTextILContrasenia;
        loginButton = binding.aISesionButtonIniciaSesion;
        cargando = binding.aISesionProgressBCargando;
        gmail = binding.aISesionImageVGmail;
        wechat = binding.aISesionImageVWechat;
        facebook = binding.aISesionImageVFacebook;
        olvidarContrasenia = binding.aISesionTextVOlvidarContrasenia;
        registrar = binding.aISesionTextVRegistrar;
        recordar = binding.aISesionCheckBRecordar;
    }

    private void mostrarInfoConSharedPreference(EditText usernameEditText, EditText passwordEditText, CheckBox recordar) {
        // Obtener una instancia de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceUtils.PREFS_CUENTA, MODE_PRIVATE);
        boolean esRecordar = sharedPreferences.getBoolean(SharedPreferenceUtils.RECORDAR, false);
        //Si en SharedPreferences tiene datos de recordar y es verdad se autocompleta.
        if (esRecordar) {
            String email = sharedPreferences.getString(SharedPreferenceUtils.EMAIL, "");
            String contrasenia = sharedPreferences.getString(SharedPreferenceUtils.CONTRASENIA, "");
            usernameEditText.setText(email);
            passwordEditText.setText(contrasenia);
            recordar.setChecked(true);
        }
    }


    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.bienvenido) + " " + model.getUser_name() + "!";
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void otraFormaDeAcceso() {
        Toast.makeText(IniciaSesion.this, "Sin implementar ", Toast.LENGTH_LONG).show();
    }
}