package com.example.debatazo.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import androidx.activity.result.contract.ActivityResultContracts;

import com.example.debatazo.R;
import com.example.debatazo.band.BandObject;
import com.example.debatazo.debaterecycler.detalle.DebateDetalle;
import com.example.debatazo.usuario.iniciarsesion.data.model.Token;
import com.example.debatazo.usuario.iniciarsesion.ui.login.IniciaSesion;

import java.text.SimpleDateFormat;
import java.util.Date;

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
}
