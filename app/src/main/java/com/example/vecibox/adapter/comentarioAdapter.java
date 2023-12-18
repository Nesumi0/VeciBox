package com.example.vecibox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vecibox.R;
import com.example.vecibox.model.Comentario;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class comentarioAdapter extends FirestoreRecyclerAdapter<Comentario, comentarioAdapter.ViewHolder> {

    public comentarioAdapter(@NonNull FirestoreRecyclerOptions<Comentario> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull comentarioAdapter.ViewHolder holder, int position, @NonNull Comentario model) {
        holder.nameU.setText("Nombre: "+model.getUsuario());
        holder.comentario.setText("Comentario: "+model.getComentario());
        holder.tipoC.setText("Tipo: "+model.getTipo());
    }

    @NonNull
    @Override
    public comentarioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_comentario_single,parent,false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameU, tipoC, comentario;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameU = itemView.findViewById(R.id.NombreUsuario);
            tipoC = itemView.findViewById(R.id.TipoComentario);
            comentario = itemView.findViewById(R.id.Comentario);
        }
    }
}
