package com.example.vecibox.Negocio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vecibox.R;
import com.example.vecibox.Usuario.MenuActivity;
import com.example.vecibox.adapter.comentarioAdapter;
import com.example.vecibox.model.Comentario;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ComentariosActivity extends AppCompatActivity {
    Button btn_coments, btn_volver;
    RecyclerView mRecycler;
    comentarioAdapter mAdapter;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);
        btn_coments = findViewById(R.id.button);
        btn_volver = findViewById(R.id.button2);
        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.recycleViewComentario);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        Query query = mFirestore.collection("Comentarios");
        FirestoreRecyclerOptions<Comentario> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Comentario>().setQuery(query, Comentario.class).build();

        mAdapter = new comentarioAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);


        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ComentariosActivity.this, MenuActivity.class));
            }
        });
        btn_coments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ComentariosActivity.this, AnadirComentariosActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}