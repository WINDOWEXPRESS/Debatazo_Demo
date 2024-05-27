package com.example.debatazo.debaterecycler.detalle;


import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.debatazo.R;
import com.example.debatazo.debaterecycler.detalle.objecto.ComentarioObjeto;
import com.example.debatazo.utils.GlobalFuntion;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MostrarMasAdaptador extends RecyclerView.Adapter<MostrarMasAdaptador.MostrarMasViewHolder> {
    public interface ItemClickListener {
        void onClick(View view, int position, ComentarioObjeto comentarioObjeto);
    }

    private boolean isEnabled = true;

    private ComentarioAdaptador.ItemClickListener clickListener;

    public void setClickListener(ComentarioAdaptador.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void setEnabled(boolean enabled){
        this.isEnabled = enabled;
    }

    private List<ComentarioObjeto> listaComentarios;
    private ComentarioObjeto comentarioObjeto;
    private Map<Integer,String> nombres;
    private Context context;

    public MostrarMasAdaptador(ComentarioObjeto comentarioObjeto,List<ComentarioObjeto> listaComentarios, Context context) {
        this.listaComentarios = listaComentarios;
        this.context = context;
        this.comentarioObjeto = comentarioObjeto;
        this.nombres = inicializarMapNombre(listaComentarios);
    }

    public class MostrarMasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ShapeableImageView perfil;
        TextView nombre, fechaP, descripcion, mostrarMas;

        public MostrarMasViewHolder(@NonNull View itemView) {
            super(itemView);
            perfil = itemView.findViewById(R.id.comentario_imageC_usuario);
            nombre = itemView.findViewById(R.id.comentario_textV_nombre);
            fechaP = itemView.findViewById(R.id.comentario_textV_fechaP);
            descripcion = itemView.findViewById(R.id.comentario_textV_descripcion);
            mostrarMas = itemView.findViewById(R.id.comentario_textV_mostrarM);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view){
            if(isEnabled){
                if(clickListener != null){
                    clickListener.onClick(view,getAdapterPosition(),listaComentarios.get(getAdapterPosition()));
                }
            }
        }
    }

    @NonNull
    @Override
    public MostrarMasAdaptador.MostrarMasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.comentario_filas, parent, false
        );
        return new MostrarMasAdaptador.MostrarMasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MostrarMasAdaptador.MostrarMasViewHolder holder, int position) {
        ComentarioObjeto comentarioObjeto = listaComentarios.get(position);

        Picasso.get().load(comentarioObjeto.getProfileImg()).into(holder.perfil);
        holder.nombre.setText(comentarioObjeto.getUserName());
        holder.nombre.append(GlobalFuntion.usuarioEquipo(context,comentarioObjeto.getType()));
        holder.nombre.append(usuarioResponde(comentarioObjeto.getPid()));
        holder.fechaP.setText(comentarioObjeto.getReleaseDate());
        holder.descripcion.setText(comentarioObjeto.getDescription());
        holder.mostrarMas.setVisibility(View.GONE);
    }
    @Override
    public int getItemCount() {return listaComentarios.size();}

    public void add(List<ComentarioObjeto> comentarios){
        int itemChanged = getItemCount();
        listaComentarios.addAll(comentarios);
        aniadirMasNombre(comentarios);
        notifyItemChanged(itemChanged);
    }

    public void addAll(List<ComentarioObjeto> comentarios){
        listaComentarios.addAll(comentarios);
        nombres = inicializarMapNombre(comentarios);
        notifyDataSetChanged();
    }

    public void clear(){
        listaComentarios.clear();
        nombres.clear();
    }

    private Spannable usuarioResponde(int pid){
        Spannable spannable = new SpannableString("");
        if(pid > 0) {
            String mesage = " " + context.getResources().getString(R.string.responder) +" "+ nombres.get(pid);
            spannable = GlobalFuntion.customText(mesage, Color.BLUE);
        }
        return spannable;
    }
    private Map<Integer,String> inicializarMapNombre(List<ComentarioObjeto> listaComentarios) {
        Map<Integer,String> nombre = new HashMap<>();
        nombre.put(comentarioObjeto.getId(),comentarioObjeto.getUserName());
        for(ComentarioObjeto comentarioObjeto : listaComentarios){
            nombre.put(comentarioObjeto.getId(),comentarioObjeto.getUserName());
        }
        return nombre;
    }

    private void aniadirMasNombre(List<ComentarioObjeto> listaComentarios) {
        for(ComentarioObjeto comentarioObjeto : listaComentarios){
            nombres.put(comentarioObjeto.getId(),comentarioObjeto.getUserName());
        }
    }
}
