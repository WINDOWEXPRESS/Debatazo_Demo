package com.example.debatazo.usuario.contraseniamanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.debatazo.R;
import com.example.debatazo.usuario.iniciarsesion.data.model.Token;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModel;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModelFactory;
import com.google.android.material.textfield.TextInputLayout;

public class ContraseniaManager extends AppCompatActivity {
    private ImageView volver;
    private TextView titulo;
    private TextView mensaje;
    private TextInputLayout email;
    private TextInputLayout contrasenia;
    private TextInputLayout contraseniaRepetido;
    private ProgressBar cargar;
    private Button confirmar;
    public static String RECUPER_KEY = "Soy clave para recuperar";
    public static String CAMBIAR_KEY = "Soy clave para cambiar";
    public static String TIPO = "tipo";
    LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrasenia_manager);

        vincularComponentes();

        volver.setOnClickListener(view -> finish());

        Intent intent = getIntent();
        String tipo = intent.getStringExtra(TIPO);

        /*
        *Hay dos tipo de viista depende de donde entre hay funciones diferentes.
        */
        if (tipo.equals(RECUPER_KEY)){
            titulo.setText("Recuperación de contraseña");
            contrasenia.setVisibility(View.GONE);
            contraseniaRepetido.setVisibility(View.GONE);

            confirmar.setOnClickListener(view -> {
                if (email.getEditText().getText() != null){
                    loginViewModel.recuperarPassword(email.getEditText().getText().toString(),mensaje);
                }
            });
        }else {
            email.setVisibility(View.GONE);

            confirmar.setOnClickListener(view -> {
                System.out.println(loginViewModel.esPasswordValido(contrasenia.getEditText().getText().toString()));
                if (!contrasenia.getEditText().getText().toString().equals(contraseniaRepetido.getEditText().getText().toString()) ){

                    mensaje.setText("Contraseña no coincide");

                }else if(contrasenia.getEditText().getText().toString().isEmpty() || contraseniaRepetido.getEditText().getText().toString().isEmpty()){

                    mensaje.setText("Hay campos vacios");

                }else if(!loginViewModel.esPasswordValido(contrasenia.getEditText().getText().toString())){

                    mensaje.setText(R.string.invalido_contrasenia);

                }else {
                    loginViewModel.cambiarPassword(Token.getInstance().getUserId(),contrasenia.getEditText().getText().toString(),mensaje);
                }
            });
        }

        loginViewModel.getLoadingLiveData().observe(this, loading -> {
            if (loading){
                confirmar.setEnabled(false);
                cargar.setVisibility(View.VISIBLE);
            }else {
                confirmar.setEnabled(true);
                cargar.setVisibility(View.GONE);
            }
        });

    }

    private void vincularComponentes() {
        volver = findViewById(R.id.aCManager_imageV_volver);
        titulo = findViewById(R.id.aCManager_textV_titulo);
        mensaje = findViewById(R.id.aCManager_tView_mensaje);
        email = findViewById(R.id.aCManager_textIL_email);
        contrasenia = findViewById(R.id.aCManager_textIL_contrasenia);
        contraseniaRepetido = findViewById(R.id.aCManager_textIL_repetirContrasenia);
        cargar = findViewById(R.id.aCManager_progressB_cargando);
        confirmar = findViewById(R.id.aCManager_button_confirmar);

        // Crear una instancia del ViewModel utilizando un ViewModelProvider y una Factory personalizada
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
    }

}