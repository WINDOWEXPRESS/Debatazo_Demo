package com.example.debatazo.perfilylogin.ui.login;

import android.app.Activity;

import androidx.annotation.DrawableRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.debatazo.R;

import com.example.debatazo.databinding.ActividadIniciaSesionBinding;
import com.example.debatazo.perfilylogin.ActividadRegistrar;

public class IniciaSesion extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private ActividadIniciaSesionBinding binding;
    private boolean esOjoAbierto = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActividadIniciaSesionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.actividadISEditTEmail;
        final EditText passwordEditText = binding.actividadISEditTContrasenia;
        final Button loginButton = binding.actividadISButtonResgistrar;
        final ProgressBar loadingProgressBar = binding.loading;
        final ImageView verContraseniaEditText = binding.actividadISImageVVerContrasenia;
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


        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        //funcion para ver contraseeña si cambiar eel icono de la imagen
        verContraseniaEditText.setOnClickListener(view -> {
            if (!esOjoAbierto){
                verContraseniaEditText.setSelected(true);
                esOjoAbierto = true;
                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                verContraseniaEditText.setImageResource(R.drawable.ocultar);
            }else {
                verContraseniaEditText.setSelected(false);
                esOjoAbierto = false;
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                verContraseniaEditText.setImageResource(R.drawable.ver);

            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
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
        String welcome = getString(R.string.welcome) + model.getDisplayName();
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