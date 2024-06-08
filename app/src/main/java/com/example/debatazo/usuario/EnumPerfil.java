package com.example.debatazo.usuario;

import com.example.debatazo.R;

public enum EnumPerfil {
    HOMBRE1(R.id.aUsuario_imageV_hombre1,R.drawable.avatar_hombre_1,"https://i.imgur.com/hLRJMfu.png"),
    HOMBRE2(R.id.aUsuario_imageV_hombre2,R.drawable.avatar_hombre_2,"https://i.imgur.com/4ZbFqHw.png"),
    HOMBRE3(R.id.aUsuario_imageV_hombre3,R.drawable.avatar_hombre_3,"https://i.imgur.com/c4ujVR1.png"),
    MUJER1(R.id.aUsuario_imageV_mujer1,R.drawable.avatar_mujer_1,"https://i.imgur.com/rC1asEd.png"),
    MUJER2(R.id.aUsuario_imageV_mujer2,R.drawable.avatar_mujer_2,"https://i.imgur.com/XniWEPh.png"),
    MUJER3(R.id.aUsuario_imageV_mujer3,R.drawable.avatar_mujer_3,"https://i.imgur.com/lFwSx6e.png"),
    MUJER4(R.id.aUsuario_imageV_mujer4,R.drawable.avatar_mujer_4,"https://i.imgur.com/gQcpftX.png");


    public final int REFERENCIA_ID;
    public final int REFERENCIA_DRAWABLE;
    public final String URL;
    
    EnumPerfil(int REFERENCIA_ID,int REFERENCIA_DRAWABLE,String URL) {
        this.REFERENCIA_ID = REFERENCIA_ID;
        this.REFERENCIA_DRAWABLE = REFERENCIA_DRAWABLE;
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }
}
