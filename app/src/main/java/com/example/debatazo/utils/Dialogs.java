package com.example.debatazo.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;

import com.example.debatazo.R;

public class Dialogs{

    public static final String W = "Alerta";
    public static final String E = "Error";
    private String title;
    private String mensaje;
    private boolean postive;
    private boolean negative;
    private Intent intent;
    private Boolean cancer;

    public Dialogs(String t, String m){
        this.title = t;
        this.mensaje = m;
    }
    public Dialogs(String t, String m, boolean p, boolean n){
        this.title = t;
        this.mensaje = m;
        this.postive = p;
        this.negative = n;
    }
    public Dialogs(String t, String m,Intent i, boolean p, boolean n){
        this.title = t;
        this.mensaje = m;
        this.intent = i;
        this.postive = p;
        this.negative = n;
    }

    public void showDialog(Context context){
        String newMensaje = "";
        Integer num;
        try {
            num = Integer.parseInt(mensaje);
            newMensaje = context.getResources().getString(num);
        }catch (NumberFormatException e){
            newMensaje = mensaje;
        }finally {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(newMensaje);
            AlertDialog dialog = builder.create();
            dialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                }
            }, GlobalConstants.DIALOG_SLEEP_TIME);
        }
    }

    public void showConfirmDialog(Context context){
        String newMensaje = "";
        Integer num;
        try {
            num = Integer.parseInt(mensaje);
            newMensaje = context.getResources().getString(num);
        }catch (NumberFormatException e){
            newMensaje = mensaje;
        }finally {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(newMensaje);
            if(postive){
                builder.setPositiveButton(R.string.aceptar,(DialogInterface d, int id)->{
                    if(intent != null){
                        context.startActivity(intent);
                    }else{
                        d.dismiss();
                    }
                });
            }
            if(negative){
                builder.setNegativeButton(R.string.cancelar,(DialogInterface d, int id)->{
                    this.cancer = true;
                    d.cancel();
                });
            }
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public boolean getCancer(){
        return this.cancer;
    }
}
