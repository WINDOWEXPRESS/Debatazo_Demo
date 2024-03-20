package com.example.debatazo.usuario.datospersonal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.debatazo.R;
import com.example.debatazo.databinding.ActividadDatosPersonalBinding;
import com.example.debatazo.usuario.iniciarsesion.data.LoginRepository;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModel;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModelFactory;
import com.squareup.picasso.Picasso;

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
    private LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActividadDatosPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vincularVista();
        mostrarInformacion();

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
        nombreUsuario = binding.actividadDPTextVNombre;
        nombrePersonal = binding.actividadDPTextVNombrePersonal;
        edad = binding.actividadDPTextVEdad;
        sexo = binding.actividadDPTextVSexo;
        perfil = binding.actividadDPImageVPerfil;

    }
    private void mostrarInformacion() {
        // Crear una instancia del ViewModel utilizando un ViewModelProvider y una Factory personalizada
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        //TODO HAY QUE HACER PRUEBA CON ISLOGEDIN()
        // Verificar si el usuario está autenticado y se ha obtenido información del usuario
        if (loginViewModel.getLoginRepository().getUser() != null) {
            // Mostrar la información del usuario en las vistas correspondientes
            descripcionPersonal.setText(loginViewModel.getLoginRepository().getUser().getSelf());
            id.setText(loginViewModel.getLoginRepository().getUser().getUser_id());
            nombreUsuario.setText(loginViewModel.getLoginRepository().getUser().getUser_name());
            nombrePersonal.setText(loginViewModel.getLoginRepository().getUser().getFull_name());
            edad.setText(loginViewModel.getLoginRepository().getUser().getAge());
            sexo.setText(loginViewModel.getLoginRepository().getUser().getSex());

            // Cargar la imagen de perfil del usuario utilizando Picasso
            Picasso.get().load(loginViewModel.getLoginRepository().getUser().getProfile_img()).into(perfil);
        }
    }

}