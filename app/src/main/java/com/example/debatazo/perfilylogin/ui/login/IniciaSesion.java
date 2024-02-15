package com.example.debatazo.perfilylogin.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.debatazo.R;

import com.example.debatazo.databinding.ActividadIniciaSesionBinding;
import com.example.debatazo.perfilylogin.ActividadRegistrar;
import com.google.android.material.textfield.TextInputLayout;

public class IniciaSesion extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private ActividadIniciaSesionBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActividadIniciaSesionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.actividadISTextILEmail.getEditText();
        final EditText passwordEditText = binding.actividadISTextILContrasenia.getEditText();
        final TextInputLayout passwordTextInputLayout = binding.actividadISTextILContrasenia;
        final Button loginButton = binding.actividadISButtonResgistrar;
        final ProgressBar loadingProgressBar = binding.loading;
        final ImageView gmail = binding.actividadISImageVGmail;
        final ImageView wechat = binding.actividadISImageVWechat;
        final ImageView facebook = binding.actividadISImageVFacebook;
        final TextView olvidarContrasenia = binding.actividadISTextVOlvidarContrasenia;
        final TextView registrar = binding.actividadISTextVRegistrar;
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                    passwordTextInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                }else {
                    passwordTextInputLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }

                setResult(Activity.RESULT_OK);
                //Complete and destroy login activity once successful
                finish();
            }
        });

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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);


        // Establece un listener para capturar el evento cuando se presiona una acción en el teclado virtual
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Verifica si la acción es "Done" en el teclado virtual
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Cuando se presiona "Done", se llama al método login del ViewModel
                    // para intentar iniciar sesión con el nombre de usuario y la contraseña proporcionados
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                // Devuelve falso para indicar que el evento no está consumido y puede ser manejado por otros listeners
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                binding.actividadISLinearLOperaciones.setVisibility(View.INVISIBLE);
                usernameEditText.setVisibility(View.INVISIBLE);
                passwordEditText.setVisibility(View.INVISIBLE);

                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        olvidarContrasenia.setOnClickListener(view -> {
            Toast.makeText(IniciaSesion.this,"Sin implementar ",Toast.LENGTH_LONG).show();
        }
        );

        gmail.setOnClickListener(view -> {
            otraFormaDeAcceso(view);
        });
        wechat.setOnClickListener(view -> {
            otraFormaDeAcceso(view);
        });
        facebook.setOnClickListener(view -> {
            otraFormaDeAcceso(view);
        });

        registrar.setOnClickListener(view -> {
            Intent i = new Intent(IniciaSesion.this,ActividadRegistrar.class);
            startActivity(i);
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.bienvenido) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
    private void otraFormaDeAcceso(View view){
        Toast.makeText(IniciaSesion.this,"Sin implementar ",Toast.LENGTH_LONG).show();
    }
}