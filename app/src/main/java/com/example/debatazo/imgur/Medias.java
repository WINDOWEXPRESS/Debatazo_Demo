package com.example.debatazo.imgur;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

import com.example.debatazo.ActividadPrincipal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class Medias {
    public final String SIMPLE_TITLE = "simple upload";
    public final String SIMPLE_DESCRIPTION = "This is a simple image upload in Imgur";
    public final String SIMPLE_TYPE = "file";
    public final String NECESITA_BODY = "Se necesita permiso para acceder a la galería";
    public final String PERMISO_TITLE = "Se necesita permiso";
    public final int READ_SDCARD_PERMISSION_REQUEST_CODE = 0;
    public final static int IMG_VIEW_SIZE = 1000;
    public final String PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    private Uri imageUri;
    public Medias(){this.imageUri = null;}
    public Uri getImageUri() {return imageUri;}
    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    //Funcion para chequear permiso de acceso a galeria
    public boolean checkPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Copia el contenido desde el URI proporcionado a un archivo temporal.
     *
     * @param uri El URI desde el cual leer el contenido.
     * @param actividad La actividad de contexto para acceder al resolutor de contenidos.
     * @return Un objeto File que representa el archivo temporal que contiene el contenido copiado.
     * @throws IOException Si ocurre un error de E/S durante el proceso de copia.
     */
    private File copyStreamToFile(Uri uri, Activity actividad) throws IOException {
        // Crea un archivo temporal con un prefijo "temp" y sin extensión de archivo.
        File outputFile = File.createTempFile("temp", null);

        try (
                // Abre un flujo de entrada desde el URI usando el resolutor de contenidos de la actividad.
                InputStream input = actividad.getContentResolver().openInputStream(uri);
                // Crea un flujo de salida para escribir en el archivo temporal.
                OutputStream output = new FileOutputStream(outputFile)
        ) {
            // Define un buffer para contener los bytes leídos.
            byte[] buffer = new byte[4 * 1024]; // Tamaño del buffer de 4 KB
            int byteCount;

            // Lee desde el flujo de entrada y escribe en el flujo de salida.
            while ((byteCount = input.read(buffer)) != -1) {
                output.write(buffer, 0, byteCount);
            }

            // Limpia el flujo de salida para asegurar que todos los datos se han escrito.
            output.flush();
        }

        // Devuelve el archivo temporal.
        return outputFile;
    }

    public MultipartBody.Part generateMultipartBody(Uri uri, Activity actividad)throws IOException{
        File file = copyStreamToFile(uri, actividad);
        RequestBody imgBody = RequestBody.create(file, MediaType.parse("image/*"));
        return MultipartBody.Part.createFormData("image", file.getName(),imgBody);
    }

    public RequestBody createTextPlainBody(String text){
        return RequestBody.create(text,MediaType.parse("text/plain"));
    }
}