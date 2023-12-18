package com.example.vecibox.Negocio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vecibox.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AnadirComentariosActivity extends AppCompatActivity {
    EditText nameU,ComentarioU,tipoC;
    Button btn_comentario;

    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_comentarios);
        nameU = findViewById(R.id.txt_nameU);
        ComentarioU = findViewById(R.id.txt_ComentU);
        tipoC = findViewById(R.id.txt_tipo);
        btn_comentario = findViewById(R.id.btn_anadirComentario);
        mFirestore = FirebaseFirestore.getInstance();

        btn_comentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameUser = nameU.getText().toString().trim();
                String ComentarioUser = ComentarioU.getText().toString().trim();
                String TipoC = tipoC.getText().toString().trim();
                if(nameUser.isEmpty() && ComentarioUser.isEmpty() && TipoC.isEmpty()){
                    Toast.makeText(AnadirComentariosActivity.this, "Ingresar los datos", Toast.LENGTH_SHORT).show();
                }else{
                    postComentarios(nameUser,ComentarioUser,TipoC);
                }
            }
        });
    }
    private void postComentarios(String nameUser, String comentarioUser, String tipoC) {
        Map<String, Object> map = new HashMap<>();
        map.put("Usuario",nameUser);
        map.put("Comentario",comentarioUser);
        map.put("Tipo",tipoC);

        mFirestore.collection("Comentarios").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                finish();
                Toast.makeText(AnadirComentariosActivity.this, "Ingresado exitosamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AnadirComentariosActivity.this, ComentariosActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AnadirComentariosActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}