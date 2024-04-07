package com.example.debatazo.configuracion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

import com.example.debatazo.R;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModel;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModelFactory;

public class Configuracion extends AppCompatActivity {
     private Button cerrarSesion;
    private LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_configuracion);

        vincularVistas();

        // Crear una instancia del ViewModel utilizando un ViewModelProvider y una Factory personalizada
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        if (!loginViewModel.getLoginRepository().isLoggedIn()){
            cerrarSesion.setEnabled(false);
        }

        cerrarSesion.setOnClickListener(view -> {
            showLogoutConfirmationDialog();
        });
    }

    private void vincularVistas() {
        cerrarSesion = findViewById(R.id.actividadC_button_cerrarSesion);

    }
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.cerrar_sesion);
        builder.setMessage(R.string.mensaje_cerrar_sesion);
        builder.setPositiveButton(R.string.si, (dialog, which) -> {
            // Aquí colocas el código para cerrar sesión
            // Llamar al método de logout de ViewModel (Repositorio)
            loginViewModel.getLoginRepository().logout(Configuracion.this);
            cerrarSesion.setEnabled(false);
            setResult(Activity.RESULT_OK);
        });
        builder.setNegativeButton(R.string.no, (dialog, which) -> {
            // El usuario canceló el cierre de sesión, no hacemos nada

        });
        builder.show();
    }

}