package com.example.debatazo.debaterecycler.detalle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.debatazo.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComentarioAdaptador extends RecyclerView.Adapter<ComentarioAdaptador.CommentarioViewHolder> {

    private List<ComentarioObjeto> listaComentarios;
    public ComentarioAdaptador(List<ComentarioObjeto> listaComentarios){this.listaComentarios = listaComentarios;}
public class CommentarioViewHolder extends RecyclerView.ViewHolder{
    ShapeableImageView perfil;
    TextView nombre;
    TextView descripcion;

    public CommentarioViewHolder(@NonNull View itemView) {
        super(itemView);
        perfil = itemView.findViewById(R.id.comentario_imagenC_usuario);
        nombre = itemView.findViewById(R.id.comentario_textV_nombre);
        descripcion = itemView.findViewById(R.id.comentario_textV_descripciones);
    }
}
    @NonNull
    @Override
    public ComentarioAdaptador.CommentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.comentario_filas,parent,false
        );
        return new ComentarioAdaptador.CommentarioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioAdaptador.CommentarioViewHolder holder, int position) {
        ComentarioObjeto comentarioObjeto = listaComentarios.get(position);

        Picasso.get().load(comentarioObjeto.getProfileImg()).into(holder.perfil);
        holder.nombre.setText(comentarioObjeto.getUserName());
        holder.descripcion.setText(comentarioObjeto.getDescription());
    }

    @Override
    public int getItemCount() {return listaComentarios.size();}

}

