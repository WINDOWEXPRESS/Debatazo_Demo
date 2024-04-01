package com.example.debatazo.configuracion;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.debatazo.R;
import com.example.debatazo.savesharedpreference.SaveSharedPreference;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModel;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModelFactory;

public class Configuracion extends AppCompatActivity {
    private Button cerrarSesion;
    private SeekBar brilloSeekBar;
    private CheckBox seguirSistema;
    private LoginViewModel loginViewModel;
    private BrilloUtils brilloUtils;
    private LinearLayout brilloLinearLayout;
    private TextView sugerencia;
    private TextView brilloTextView;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_configuracion);

        // Vincular vistas
        vincularVistas();

        //Esperar el resultado devuelto de permiso
        ActivityResultLauncher<Intent> requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (Settings.System.canWrite(Configuracion.this)) {
                        // El usuario otorgó el permiso poner visible el ajuste de brillo
                        brilloLinearLayout.setVisibility(View.VISIBLE);
                    } else {
                        // El usuario denegó el permiso
                    }
                });

        //Cuando usuario click brillo pregunta si permitir Modificar la configuración
        brilloTextView.setOnClickListener(view -> {
            allowModifySettings( requestPermissionLauncher);
        });

        //Obtener instancia de BrilloUtils
        brilloUtils = BrilloUtils.getInstancia();

        // Observar cambios en el valor brilloApp de la brilloUtils
        brilloUtils.getBrilloAppLD().observe(this,integer -> {
            brilloUtils.setAppBrillo(this,integer);
        });

        // Obtener una referencia a SharedPreferences para almacenar y recuperar el valor del brillo de la pantalla
        sharedPreferences = getSharedPreferences(SaveSharedPreference.PREFS_BRILLO, MODE_PRIVATE);

        //Ajustar el estado correspondiente a valor guardado en sharedPreferences
        seguirSistema.setChecked(sharedPreferences.getBoolean(SaveSharedPreference.BRILLO_SEGUIR_SISTEMA, false));

        //Comprobacion de permiso cada vez que entre a configuracion
        if(Settings.System.canWrite(Configuracion.this)){

            // El usuario otorgó el permiso poner visible el ajuste de brillo
            brilloLinearLayout.setVisibility(View.VISIBLE);
            if (seguirSistema.isChecked()){
                brilloSeekBar.setEnabled(false);

                //AQUI *2 PORQUE EL VALOR OBTENIDO NO SE PORQUE ES LA MITAD MENOR
                brilloUtils.setBrilloSistemaMLD(brilloUtils.obtenerBrilloPantallaSystema(Configuracion.this)*2);
                brilloSeekBar.setProgress(brilloUtils.getBrilloSistemaLD().getValue());
            }else {
                //Si no sigue el brillo de sistema.
                // Obtener el valor de brillo de App si es null obtener por primera vez de brillo de sistema y configurarlo
                if (brilloUtils.getBrilloAppLD().getValue()==null){
                    //AQUI *2 PORQUE EL VALOR OBTENIDO NO SE PORQUE ES LA MITAD MENOR
                    brilloUtils.setBrilloAppMLD(brilloUtils.obtenerBrilloPantallaSystema(Configuracion.this)*2);
                }
                brilloUtils.setAppBrillo(this,brilloUtils.getBrilloAppLD().getValue()*2);
                brilloSeekBar.setProgress(brilloUtils.getBrilloAppLD().getValue());
            }
        }


        // Configurar estado de la opción "seguir sistema"
        configurarSeguirSistemaCheckBox();

        // Escuchar cambios en la barra de brillo
        brilloSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // Mientras se mueve ,ajusta el brillo
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                brilloUtils.setBrilloAppMLD(i);
            }

            // Cuando empieza a mover verifica si tiene permiso
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            // Cuando para de mover
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Crear una instancia del ViewModel utilizando un ViewModelProvider y una Factory personalizada
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        // Validar si el usuario está logueado y habilitar/deshabilitar el botón "Cerrar Sesión"
        if (!loginViewModel.getLoginRepository().isLoggedIn()) {
            cerrarSesion.setEnabled(false);
        }

        // Configurar el botón "Cerrar Sesión"
        cerrarSesion.setOnClickListener(view -> {
            showLogoutConfirmationDialog();
        });

    }

    private void vincularVistas() {
        cerrarSesion = findViewById(R.id.actividadC_button_cerrarSesion);
        brilloSeekBar = findViewById(R.id.actividadC_SeekB_brillo);
        seguirSistema = findViewById(R.id.actividadC_checkB_seguirSistema);
        brilloLinearLayout = findViewById(R.id.actividadC_linearLV_brillo);
        sugerencia = findViewById(R.id.actividadC_textV_sugerencia);
        brilloTextView = findViewById(R.id.actividadC_textV_Brillo);
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.cerrar_sesion);
        builder.setMessage(R.string.mensaje_cerrar_sesion);
        builder.setPositiveButton(R.string.si, (dialog, which) -> {
            // Aquí colocas el código para cerrar sesión
            // Llamar al método de logout de ViewModel (Repositorio)
            loginViewModel.getLoginRepository().logout();
            cerrarSesion.setEnabled(false);
            setResult(Activity.RESULT_OK);
        });
        builder.setNegativeButton(R.string.no, (dialog, which) -> {
            // El usuario canceló el cierre de sesión, no hacemos nada

        });
        builder.show();
    }
    private void configurarSeguirSistemaCheckBox    () {
        seguirSistema.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            // Editor de SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Verifica si el CheckBox está marcado o desmarcado
            if (isChecked) {// El CheckBox está marcado
                //Guardar opcion en un SharedPreference
                editor.putBoolean(SaveSharedPreference.BRILLO_SEGUIR_SISTEMA, true);

                //Obtener el valor de brillo de sistema y configurarlo
                //AQUI *2 PORQUE EL VALOR OBTENIDO NO SE PORQUE ES LA MITAD MENOR
                brilloUtils.setBrilloSistemaMLD(brilloUtils.obtenerBrilloPantallaSystema(Configuracion.this)*2);

                brilloUtils.setAppBrillo(this,brilloUtils.getBrilloSistemaLD().getValue());

                //Activar barra de brillo y configurar el valor a lo de sistema
                brilloSeekBar.setProgress(brilloUtils.getBrilloSistemaLD().getValue());
                brilloSeekBar.setEnabled(false);
            } else {// El CheckBox está desmarcado
                //Guardar opcion en un SharedPreference
                editor.putBoolean(SaveSharedPreference.BRILLO_SEGUIR_SISTEMA, false);

                brilloSeekBar.setEnabled(true);
            }
            editor.apply(); // Guardar los cambios
        });
    }

    /**
     * Aplicación no firmada por el sistema que guía al usuario para autorizar
     * manualmente los cambios en los permisos de Configuración.
     **/
    private void allowModifySettings( ActivityResultLauncher<Intent> requestPermissionLauncher) {
        // Settings.System.canWrite(MainActivity.this)
        // 检测是否拥有写入系统 Settings 的权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(Configuracion.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_date);
            builder.setTitle("Permiso para modificar el brillo de la pantalla");
            builder.setMessage("Haga clic en PERMITIR para abrir");
            // 拒绝, 无法修改
            builder.setNegativeButton("RECHAZAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(Configuracion.this, "Permiso denegado.", Toast.LENGTH_LONG).show();
                }
            });
            builder.setPositiveButton("PERMITIR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 打开允许修改Setting 权限的界面
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                    requestPermissionLauncher.launch(intent);
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }
}