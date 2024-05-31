package com.example.debatazo.debaterecycler;

import android.annotation.SuppressLint;
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

    public class debateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ShapeableImageView perfil;
        TextView nombre, fecha, titulo, fDDetalle_textV_comentario,fDDetalle_textV_corazon;
        ImageView image;

        public debateViewHolder(@NonNull View itemView) {
            super(itemView);
            perfil = itemView.findViewById(R.id.fDDetalle_imagenC_usuario);
            nombre = itemView.findViewById(R.id.fDDetalle_textV_nombre);
            fecha = itemView.findViewById(R.id.fDDetalle_textV_fecha);
            titulo = itemView.findViewById(R.id.fDDetalle_textV_titulo);
            image = itemView.findViewById(R.id.fDDetalle_imageV);
            fDDetalle_textV_corazon = itemView.findViewById(R.id.fDDetalle_textV_corazon);
            fDDetalle_textV_comentario  = itemView.findViewById(R.id.fDDetalle_textV_comentario);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick(view, getAdapterPosition(), listaDebate.get(getAdapterPosition()));
            }
        }
    }

    @NonNull
    @Override
    public debateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.fragmento_debate_detalle, parent, false
        );
        return new debateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull debateViewHolder holder, int position) {
        DebateProducto debateProducto = listaDebate.get(position);

        Picasso.get().load(debateProducto.getProfileImg()).into(holder.perfil);
        holder.nombre.setText(debateProducto.getUserName());
        holder.fecha.setText(debateProducto.getReleaseDate());
        holder.titulo.setText(debateProducto.getTitle());
        if (debateProducto.getImageUrl() != null) {
            Picasso.get().load(debateProducto.getImageUrl()).into(holder.image);
        }else{
            holder.image.setVisibility(View.GONE);
        }
        holder.fDDetalle_textV_corazon.setText(String.valueOf(debateProducto.getLike()));
        holder.fDDetalle_textV_comentario.setText(String.valueOf(debateProducto.getComment()));
    }

    @Override
    public int getItemCount() {
        return listaDebate.size();
    }

    public void add(List<DebateProducto> debates){
        int itemChanged = getItemCount();
        listaDebate.addAll(debates);
        notifyItemChanged(itemChanged);
    }

}
