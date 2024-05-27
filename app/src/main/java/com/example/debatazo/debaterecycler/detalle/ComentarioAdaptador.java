package com.example.debatazo.debaterecycler.detalle;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.debatazo.R;
import com.example.debatazo.band.BandObject;
import com.example.debatazo.debaterecycler.detalle.objecto.ComentarioObjeto;
import com.example.debatazo.utils.GlobalFuntion;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;
import java.util.List;


public class ComentarioAdaptador extends RecyclerView.Adapter<ComentarioAdaptador.CommentarioViewHolder> {
    public interface ItemClickListener {
        void onClick(View view, int position, ComentarioObjeto comentarioObjeto);
    }
    private boolean isEnabled = true;

    private ItemClickListener clickListener;

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void setEnabled(boolean enabled){
        this.isEnabled = enabled;
    }

    private List<ComentarioObjeto> listaComentarios;
    private Context context;
    private int debateId;
    private BandObject selectBand;
    private FragmentManager fragmentManager;
    private static final String MOSTRARMASDIALOG = "MostrarMDialog";
    public ComentarioAdaptador(List<ComentarioObjeto> listaComentarios,
                               Context context,
                               int debateId,
                               BandObject selectBand,
                               FragmentManager fragmentManager) {
        this.listaComentarios = listaComentarios;
        this.context = context;
        this.debateId = debateId;
        this.selectBand = selectBand;
        this.fragmentManager = fragmentManager;
    }

    public class CommentarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ShapeableImageView perfil;
        TextView nombre, descripcion, mostrarMas, fechaPublica;
        ;

        public CommentarioViewHolder(@NonNull View itemView) {
            super(itemView);
            perfil = itemView.findViewById(R.id.comentario_imageC_usuario);
            nombre = itemView.findViewById(R.id.comentario_textV_nombre);
            fechaPublica = itemView.findViewById(R.id.comentario_textV_fechaP);
            descripcion = itemView.findViewById(R.id.comentario_textV_descripcion);
            mostrarMas = itemView.findViewById(R.id.comentario_textV_mostrarM);

            perfil.setOnClickListener(this);
            nombre.setOnClickListener(this);
            fechaPublica.setOnClickListener(this);
            descripcion.setOnClickListener(this);
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
    public ComentarioAdaptador.CommentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.comentario_filas, parent, false
        );
        return new ComentarioAdaptador.CommentarioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioAdaptador.CommentarioViewHolder holder, int position) {
        ComentarioObjeto comentarioObjeto = listaComentarios.get(position);

        Picasso.get().load(comentarioObjeto.getProfileImg()).into(holder.perfil);
        holder.nombre.setText(comentarioObjeto.getUserName().concat(" "));
        holder.nombre.append(GlobalFuntion.usuarioEquipo(context,comentarioObjeto.getType()));
        holder.fechaPublica.setText(comentarioObjeto.getReleaseDate());
        holder.descripcion.setText(comentarioObjeto.getDescription());

        if(comentarioObjeto.getChildren().size() > 0){
            holder.mostrarMas.setVisibility(View.VISIBLE);
            holder.mostrarMas.setOnClickListener(view ->{
                construirDialog(comentarioObjeto);
            });
        }else{
            holder.mostrarMas.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {return listaComentarios.size();}

    public void add(List<ComentarioObjeto> comentarios){
        int itemChanged = getItemCount();
        listaComentarios.addAll(comentarios);
        notifyItemChanged(itemChanged);
    }

    public void addAll(List<ComentarioObjeto> comentarios){
        listaComentarios.addAll(comentarios);
        notifyDataSetChanged();
    }

    public void clear(){
        listaComentarios.clear();
    }

    private void construirDialog(ComentarioObjeto comentarioObjeto){
        DialogMostrarMFragment dialogMostrarMMFragment = DialogMostrarMFragment.Instance(comentarioObjeto,debateId,selectBand);
        dialogMostrarMMFragment.show(fragmentManager,MOSTRARMASDIALOG);

    }

    public void cambiarSelectBand(BandObject band){
        this.selectBand = band;
    }
}

