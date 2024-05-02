package com.example.debatazo.configuracion.view;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.debatazo.R;
import com.example.debatazo.configuracion.viewmodel.PrivacidadViewModel;

import java.util.Locale;

public class Privacidad extends AppCompatActivity {

    private WebView webView ;
    private PrivacidadViewModel privacidadViewModel;
    private ImageView volver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_privacidad);

        // Vincular vistas
        vincularVistas();

        // Obtener el idioma configurado en la aplicación
        String idioma = privacidadViewModel.obtenerIdiomaConfigurado(getResources());

        // Cargar la URL de privacidad según el idioma
        String rutaPrivacidad = privacidadViewModel.obtenerUrlPrivacidad(idioma);
        mostrarWeb(rutaPrivacidad);

        volver.setOnClickListener(view -> {
            finish();
        });
    }

    private void vincularVistas() {
        webView = findViewById(R.id.aPrivacidad_webV_html);
        volver = findViewById(R.id.aPrivacidad_imageV_volver);

        // Obtener instancia del ViewModel
        privacidadViewModel = new ViewModelProvider(this).get(PrivacidadViewModel.class);

    }

    public void mostrarWeb(String ruta){

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(ruta);

    }
}
