package com.example.debatazo.debaterecycler;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DebateProducto implements Serializable {
    private Integer idUsuario;
    private Integer idDebate;
    private String imagenUsuario;
    private Date fechaPublicacion;
    private String nombreUsuario;
    private String titulo;
    private String contenido;
    private String imagenUrl;

    public DebateProducto(Integer idUsuario, Integer idDebate, String imagenUsuario, Date fechaPublicacion, String nombreUsuario,String titulo, String contenido, String imagenUrl) {
        this.idUsuario = idUsuario;
        this.idDebate = idDebate;
        this.imagenUsuario = imagenUsuario;
        this.fechaPublicacion = fechaPublicacion;
        this.nombreUsuario = nombreUsuario;
        this.titulo = titulo;
        this.contenido = contenido;
        this.imagenUrl = imagenUrl;
    }
    public String getImagenUsuario(){
        return imagenUsuario;
    }
    public String getFechaPublicacion() {return new SimpleDateFormat("dd/mm/yyyy").format(fechaPublicacion);}
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getContenido() {
        return contenido;
    }
    public String getImagenUrl() {
        return imagenUrl;
    }

}
