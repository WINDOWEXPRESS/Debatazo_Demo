package com.example.debatazo.configuracion;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.view.WindowManager;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.debatazo.savesharedpreference.SaveSharedPreference;

public class BrilloUtils {
    private static BrilloUtils instancia;

    //Metodo singleton
    public static BrilloUtils getInstancia() {
        if (instancia == null) {
            instancia = new BrilloUtils();
        }
        return instancia;
    }

    private MutableLiveData<Integer> brilloSistemaMLD = new MutableLiveData<>();

    public LiveData<Integer> getBrilloSistemaLD() {
        return brilloSistemaMLD;
    }

    private MutableLiveData<Integer> brilloAppMLD = new MutableLiveData<>();

    public LiveData<Integer> getBrilloAppLD() {
        return brilloAppMLD;
    }

    // Determinar si está activado el ajuste automático del brillo
    public static boolean esAutoBrillo(Context context) {

        boolean esAutoBrillo = false;

        try {

            esAutoBrillo = Settings.System.getInt(

                    context.getContentResolver(),

                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;

        } catch (Settings.SettingNotFoundException e) {

            e.printStackTrace();

        }

        return esAutoBrillo;

    }

    // Obtener brillo de la pantalla actual
    public int obtenerBrilloPantallaSystema(Context context) {

        int valorBrilloAhora = 0;

        ContentResolver resolver = context.getContentResolver();

        try {

            valorBrilloAhora = android.provider.Settings.System.getInt(

                    resolver, Settings.System.SCREEN_BRIGHTNESS);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return valorBrilloAhora;

    }

    public void setBrilloSistemaMLD(int brilloSistemaMLD) {
        this.brilloSistemaMLD.setValue(brilloSistemaMLD);
    }

    public void setBrilloAppMLD(int brilloAppMLD) {
        this.brilloAppMLD.setValue(brilloAppMLD);
    }

    // 设置亮度
    // 程序退出之后亮度失效
    public void setAppBrillo(Context context, int valor) {

        // Si el brillo automático está activado, desactívarlo,cuando termina vuelve a activarlo.
        if (esAutoBrillo(context)) {

            desactivarAutoBrillo(context);

            // context casting a Activity
            Activity activity = (Activity) context;

            //obtiene los atributos de la ventana de la actividad actual
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

            // Excepcion
            if (brilloAppMLD.getValue() < 1) {

                brilloAppMLD.setValue(1);

            }
            // Excepcion
            if (brilloAppMLD.getValue() > 255) {

                brilloAppMLD.setValue(255);

            }
            //Los valores de brillo generalmente se expresan en un rango de 0 a 1
            lp.screenBrightness = Float.valueOf(valor) * (1f / 255f);

            activity.getWindow().setAttributes(lp);

            activartAutoBrillo(context);
        }else {
            // context casting a Activity
            Activity activity = (Activity) context;

            //obtiene los atributos de la ventana de la actividad actual
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

            // Excepcion
            if (brilloAppMLD.getValue() < 1) {

                brilloAppMLD.setValue(1);

            }
            // Excepcion
            if (brilloAppMLD.getValue() > 255) {

                brilloAppMLD.setValue(255);

            }
            //Los valores de brillo generalmente se expresan en un rango de 0 a 1
            lp.screenBrightness = Float.valueOf(valor) * (1f / 255f);

            activity.getWindow().setAttributes(lp);
        }
    }

    public void desactivarAutoBrillo(Context context) {

        Settings.System.putInt(context.getContentResolver(),

                Settings.System.SCREEN_BRIGHTNESS_MODE,

                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

    }

    public void activartAutoBrillo(Context context) {

        Settings.System.putInt(context.getContentResolver(),

                Settings.System.SCREEN_BRIGHTNESS_MODE,

                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);

    }

    // 设置系统亮度
    // 程序退出之后亮度依旧有效

    public static void setSystemBrightness(Context context, int brightness) {
        // Excepcion
        if (brightness < 1) {

            brightness = 1;

        }
        // Excepcion
        if (brightness > 255) {

            brightness = 255;

        }

        saveBrightness(context, brightness);

    }

    // 保存亮度设置状态
    public static void saveBrightness(Context context, int brightness) {

        Uri uri = android.provider.Settings.System

                .getUriFor("screen_brightness");

        android.provider.Settings.System.putInt(context.getContentResolver(),

                "screen_brightness", brightness);

        context.getContentResolver().notifyChange(uri, null);

    }

    public void brilloAppVista(Context context){
        SharedPreferences sharedPreferences;

        BrilloUtils brilloUtils = BrilloUtils.getInstancia();
        sharedPreferences  = context.getSharedPreferences(SaveSharedPreference.PREFS_BRILLO, MODE_PRIVATE);
        //Si el opcion de seguir el Brillo de sistema esta desactivado.
        if(!sharedPreferences.getBoolean(SaveSharedPreference.BRILLO_SEGUIR_SISTEMA, true)){
            brilloUtils.getBrilloAppLD().observe((LifecycleOwner) context, integer -> {
                brilloUtils.setAppBrillo(context,integer);
            });
        }

        brilloUtils.getBrilloAppLD().observe((LifecycleOwner) context, integer -> {
            brilloUtils.setAppBrillo(context,integer);
        });

    }
}
