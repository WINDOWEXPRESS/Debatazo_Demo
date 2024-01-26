package com.example.debatazo.debaterecycler;

import java.io.Serializable;
import java.util.Date;

public class DebateProducto implements Serializable {
    private Integer idUsuario;
    private Integer idDebate;
    private String imagenUsuario;
    private Date fechaPublicacion;
    private String nombreUsuario;
    private String contenido;
    private String imagenUrl;

    public DebateProducto(Integer idUsuario, Integer idDebate, String imagenUsuario, Date fechaPublicacion, String nombreUsuario, String contenido, String imagenUrl) {
        this.idUsuario = idUsuario;
        this.idDebate = idDebate;
        this.imagenUsuario = imagenUsuario;
        this.fechaPublicacion = fechaPublicacion;
        this.nombreUsuario = nombreUsuario;
        this.contenido = contenido;
        this.imagenUrl = imagenUrl;
    }
    public String getImagenUsuario(){
        return imagenUsuario;
    }
    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContenido() {
        return contenido;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }
}
