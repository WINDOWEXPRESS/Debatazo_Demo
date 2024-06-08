package com.example.debatazo.utils;

import static androidx.core.content.ContextCompat.startActivity;

import static com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.Reflection.getPackageName;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import androidx.activity.result.contract.ActivityResultContracts;

import com.example.debatazo.R;
import com.example.debatazo.band.BandObject;
import com.example.debatazo.debaterecycler.detalle.DebateDetalle;
import com.example.debatazo.usuario.iniciarsesion.data.model.Token;
import com.example.debatazo.usuario.iniciarsesion.ui.login.IniciaSesion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GlobalFuntion {

    public static Spannable usuarioEquipo(Context context, char type){
        Spannable spannable = new SpannableString("");
        if(type == BandObject.P){
            spannable = customText(context.getResources().getString(R.string.a_favor), Color.GREEN);
        }else if(type == BandObject.N){
            spannable = customText(context.getResources().getString(R.string.en_contra).concat(" "),Color.RED);
        }
        return spannable;
    }

    public static Spannable customText(String text, int color){
        Spannable spannable = new SpannableString(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spannable.setSpan(colorSpan,0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }
    public static void showSettingsDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.necesita_permiso));
        builder.setMessage(context.getResources().getString(R.string.necesita_permiso_galeria));
        builder.setPositiveButton(context.getResources().getString(R.string.ir_configuracion), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public static Date getDateByString(String date){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean comparaTiempo(String date){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            Date expiracion = sdf.parse(date);
            Date ahora = new Date();
            int compare = ahora.compareTo(expiracion);

            return compare <= 0;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
