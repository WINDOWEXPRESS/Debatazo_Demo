package com.example.debatazo.debaterecycler;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.debatazo.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class debateAdapte extends RecyclerView.Adapter<debateAdapte.debateViewHolder> {

    private List<debateItem> listaDebate;
    public debateAdapte(List<debateItem> listaDebate) {
        this.listaDebate = listaDebate;
    }
    public static class debateViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView perfil;
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

        Picasso.get().load(debateItem.getImagenUsuario()).into(holder.perfil);
        holder.nombre.setText(debateItem.getNombreUsuario());
        holder.fecha.setText(new SimpleDateFormat("dd/mm/yyyy").format(debateItem.getFechaPublicacion()));
        holder.contenido.setText(debateItem.getContenido());
        Picasso.get().load(debateItem.getImagenUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return listaDebate.size();
    }

}
