package com.example.debatazo.debaterecycler;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.debatazo.R;
import com.example.debatazo.imagenCircular;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

public class debateAdapte extends RecyclerView.Adapter<debateAdapte.debateViewHolder> {

    private List<debateItem> listaDebate;
    public debateAdapte(List<debateItem> listaDebate) {
        this.listaDebate = listaDebate;
    }
    public static class debateViewHolder extends RecyclerView.ViewHolder{
        imagenCircular perfil;
        TextView nombre;
        TextView fecha;
        TextView contenido;
        ImageView image;

        public debateViewHolder(@NonNull View itemView) {
            super(itemView);
            perfil = itemView.findViewById(R.id.debateRV_imagenC_usuario);
            nombre = itemView.findViewById(R.id.debateRV_textV_nombre);
            fecha = itemView.findViewById(R.id.debateRV_textV_fecha);
            contenido = itemView.findViewById(R.id.debateRV_textV_contenido);
            image = itemView.findViewById(R.id.debateRV_imageV);
        }
    }
    @NonNull
    @Override
    public debateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.debate_recycler_view,parent,false
        );
        return new debateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull debateViewHolder holder, int position) {
        debateItem debateItem = listaDebate.get(position);
        URL perfilUrl = null,contenidoUrl = null;
        Bitmap perfilBitmap = null,contenidoBitmap = null;
        try {
            perfilUrl = new URL(debateItem.imagenUsuario);
            perfilBitmap = BitmapFactory.decodeStream(perfilUrl.openStream());
            contenidoUrl = new URL(debateItem.imagenUrl);
            contenidoBitmap = BitmapFactory.decodeStream(contenidoUrl.openStream());
        }catch (Exception e){}

        holder.perfil.setImageBitmap(perfilBitmap);
        holder.nombre.setText(debateItem.nombreUsuario);
        holder.fecha.setText(new SimpleDateFormat("dd/mm/yyyy").format(debateItem.fechaPublicacion));
        holder.contenido.setText(debateItem.contenido);
        holder.image.setImageBitmap(contenidoBitmap);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
