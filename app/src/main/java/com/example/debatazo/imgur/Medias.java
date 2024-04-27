package com.example.debatazo.imgur;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class Medias {
    public final int READ_SDCARD_PERMISSION_REQUEST_CODE = 0;
    public final static int IMG_VIEW_SIZE = 1000;
    public final String PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    private Uri imageUri;
    private ActivityResultLauncher<String> requestResultLauncher;
    private ActivityResultLauncher<Intent> resultLauncher;
    public Medias(){this.imageUri = null;}
    public Uri getImageUri() {return imageUri;}

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public boolean checkPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public File copyStreamToFile(Uri uri,Activity activity) throws IOException{
        File outputFile = File.createTempFile("temp", null);

        try (InputStream input = activity.getContentResolver().openInputStream(uri);
             OutputStream output = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[4 * 1024]; // buffer size
            int byteCount;
            while ((byteCount = input.read(buffer)) != -1) {
                output.write(buffer, 0, byteCount);
            }
            output.flush();
        }
        return outputFile;
    }
}