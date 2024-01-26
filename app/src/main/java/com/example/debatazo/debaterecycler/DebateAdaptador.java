package com.example.debatazo.debaterecycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.debatazo.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class DebateAdaptador extends RecyclerView.Adapter<DebateAdaptador.debateViewHolder> {

    public interface ItemClickListener {
        void onClick(View view, int position, DebateProducto producto);
    }
    private ItemClickListener clickListener;

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    private List<DebateProducto> listaDebate;
    public DebateAdaptador(List<DebateProducto> listaDebate) {
        this.listaDebate = listaDebate;
    }
    public class debateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition(),listaDebate.get(getAdapterPosition()));
        }
    }
    @NonNull
    @Override
    public debateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.fragmento_debate_recycler_view,parent,false
        );
        return new debateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull debateViewHolder holder, int position) {
        DebateProducto debateProducto = listaDebate.get(position);

        Picasso.get().load(debateProducto.getImagenUsuario()).into(holder.perfil);
        holder.nombre.setText(debateProducto.getNombreUsuario());
        holder.fecha.setText(new SimpleDateFormat("dd/mm/yyyy").format(debateProducto.getFechaPublicacion()));
        holder.contenido.setText(debateProducto.getContenido());
        Picasso.get().load(debateProducto.getImagenUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {return listaDebate.size();}

}
