package com.example.vecibox.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.vecibox.Negocio.ComentariosActivity;
import com.example.vecibox.R;
import com.example.vecibox.model.Negocio;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class negocioAdapter extends FirestoreRecyclerAdapter<Negocio, negocioAdapter.ViewHolder> {

    public negocioAdapter(@NonNull FirestoreRecyclerOptions<Negocio> options, Context context) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Negocio model) {
        holder.nombre.setText(model.getNombre());
        holder.descripcion.setText(model.getDescripcion());
        holder.horario.setText(model.getHorario());
        holder.operaciones.setText(model.getOperaciones());
        holder.btn_comentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ComentariosActivity.class);
                context.startActivity(intent);
            }
        });

        String urlDeLaImagen = model.getUrl_imagen_negocio();
        if (urlDeLaImagen != null && !urlDeLaImagen.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(urlDeLaImagen)
                    .apply(RequestOptions.placeholderOf(R.drawable.baseline_remove_24))
                    .into(holder.foto);
        } else {
            holder.foto.setImageResource(R.drawable.baseline_remove_24);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_negocio_single, parent,false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre,descripcion,horario,operaciones;
        ImageView foto;

        Button btn_comentarios;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreNS);
            descripcion = itemView.findViewById(R.id.DescripcionNS);
            horario = itemView.findViewById(R.id.HorarioNS);
            operaciones = itemView.findViewById(R.id.OperacioneNS);
            foto = itemView.findViewById(R.id.fotoNS);
            btn_comentarios = itemView.findViewById(R.id.btn_comments);
        }
    }
}
