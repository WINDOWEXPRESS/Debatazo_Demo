package com.example.debatazo.configuracion;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.view.WindowManager;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BrilloUtils {
    private static BrilloUtils instancia;
    private final int VALOR_MINIMO = 1;
    private final int VALOR_MAXIMO = 255;

    //Metodo singleton
    public static BrilloUtils getInstancia() {
        if (instancia == null) {
            instancia = new BrilloUtils();
        }
        return instancia;
    }

    private MutableLiveData<Integer> brilloAppMLD = new MutableLiveData<>();

    public LiveData<Integer> getBrilloAppLD() {
        return brilloAppMLD;
    }

    public void setBrilloAppMLD(int brilloAppMLD) {
        this.brilloAppMLD.setValue(brilloAppMLD);
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

    // 设置亮度
    // 程序退出之后亮度失效
    public void setAppBrillo(Context context, int valor) {
        // context casting a Activity
        Activity activity = (Activity) context;

        //obtiene los atributos de la ventana de la actividad actual
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

        // Excepcion
        if (brilloAppMLD.getValue() < VALOR_MINIMO || valor < VALOR_MINIMO ) {

            brilloAppMLD.setValue(VALOR_MINIMO);

        }
        // Excepcion
        if (brilloAppMLD.getValue() > VALOR_MAXIMO || valor > VALOR_MINIMO ) {

            brilloAppMLD.setValue(VALOR_MAXIMO);

        }

        // Si el brillo automático está activado, desactívarlo,cuando termina vuelve a activarlo.
        if (esAutoBrillo(context)) {

            desactivarAutoBrillo(context);

            //Los valores de brillo generalmente se expresan en un rango de 0 a 1
            lp.screenBrightness = Float.valueOf(valor) / (VALOR_MAXIMO);
            activity.getWindow().setAttributes(lp);

            activartAutoBrillo(context);
        } else {

            //Los valores de brillo generalmente se expresan en un rango de 0 a 1
            lp.screenBrightness = Float.valueOf(valor) / (VALOR_MAXIMO);
            activity.getWindow().setAttributes(lp);
        }
        setBrilloAppMLD(valor);
    }

    public static void desactivarAutoBrillo(Context context) {

        Settings.System.putInt(context.getContentResolver(),

                Settings.System.SCREEN_BRIGHTNESS_MODE,

                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

    }

    public static void activartAutoBrillo(Context context) {

        Settings.System.putInt(context.getContentResolver(),

                Settings.System.SCREEN_BRIGHTNESS_MODE,

                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);

    }

    //observador a brillo para cualquier ventana
    public void brilloAppObserver(Context context) {
        BrilloUtils brilloUtils = BrilloUtils.getInstancia();

        brilloUtils.getBrilloAppLD().observe((LifecycleOwner) context, integer -> {
            // context casting a Activity
            Activity activity = (Activity) context;
            //obtiene los atributos de la ventana de la actividad actual
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

            lp.screenBrightness = Float.valueOf(integer) / (VALOR_MAXIMO);
            activity.getWindow().setAttributes(lp);
        });

    }

/*
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

        //Este metodo cambia el brillo de systema directamente
    private void changeScreenBrightness(Context context, int screenBrightnessValue) {
        // Cambiar el modo de cambio de brillo de la pantalla a manual.
        Settings.System.putInt(
                context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        );

        // Aplicar el valor de brillo de la pantalla al sistema, esto cambiará
        // el valor en Configuración ---> Pantalla ---> Nivel de brillo.
        // También cambiará el brillo de la pantalla para el dispositivo.
        Settings.System.putInt(
                context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, screenBrightnessValue
        );
    }
*/

}
