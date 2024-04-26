package com.example.debatazo.configuracion.viewmodel;

import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.lifecycle.ViewModel;

public class PrivacidadViewModel extends ViewModel {

    public String obtenerUrlPrivacidad(String language) {
        if (language.equals("es")) {
            return "file:///android_asset/Privacidad_Es.html";
        } else {
            return "file:///android_asset/Privacidad_En.html";
        }
    }

    public String obtenerIdiomaConfigurado(Resources resources) {
        // Obtener el objeto Configuration
        Configuration configuration = resources.getConfiguration();

        return configuration.locale.getLanguage();
    }
}