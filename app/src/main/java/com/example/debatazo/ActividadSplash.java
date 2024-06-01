package com.example.debatazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.debatazo.utils.GlobalConstants;

public class ActividadSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_splash);

        ImageView imagen = findViewById(R.id.aSplash_imageV_logo);

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        // Night mode is active, we're at night!
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            imagen.setImageResource(R.drawable.logo_white);
            imagen.setBackgroundColor(Color.BLACK);
        }
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(ActividadSplash.this, ActividadPrincipal.class);
            startActivity(intent);
            finish();
        }, GlobalConstants.SPLASH_SLEEP_TIME);

    }
}