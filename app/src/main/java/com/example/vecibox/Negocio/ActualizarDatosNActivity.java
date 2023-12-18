package com.example.vecibox.Negocio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vecibox.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActualizarDatosNActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    EditText nombre, informacion, gmail, operacion, horarios;
    ImageView fotonegocio;
    Button btn_add, btn_back;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_datos_nactivity);

        mAuth = FirebaseAuth.getInstance();
        nombre = findViewById(R.id.nombreN);
        informacion = findViewById(R.id.InfoN);
        gmail = findViewById(R.id.gmailN);
        operacion = findViewById(R.id.OpeN);
        horarios = findViewById(R.id.HorarioN);
        fotonegocio = findViewById(R.id.imageN);
        btn_add = findViewById(R.id.btn_añadir);
        btn_back = findViewById(R.id.btn_back);
        mFirestore = FirebaseFirestore.getInstance();
        String id = getIntent().getStringExtra("userID");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActualizarDatosNActivity.this,MantenedorNegocioActivity.class));
            }
        });

        fotonegocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        //getLugar(id);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameN = nombre.getText().toString().trim();
                String infoN = informacion.getText().toString().trim();
                String operaN = operacion.getText().toString().trim();
                String horarioN = horarios.getText().toString().trim();

                if (nameN.isEmpty() && infoN.isEmpty() && operaN.isEmpty() && horarioN.isEmpty()) {
                    Toast.makeText(ActualizarDatosNActivity.this, "Ingresar los datos para actualizar", Toast.LENGTH_SHORT).show();
                } else {
                    UpdateNegocio(nameN, infoN, horarioN, operaN );
                }
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            fotonegocio.setImageURI(imageUri);
        }
    }

    private void UpdateNegocio(String nameN, String infoN, String horarioN, String operaN) {

        String userId = mAuth.getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("Negocios")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String negocioId = documentSnapshot.getId();

                            Map<String, Object> updateMap = new HashMap<>();
                            updateMap.put("Nombre", nameN);
                            updateMap.put("Descripcion", infoN);
                            updateMap.put("Horario", horarioN);
                            updateMap.put("Operaciones", operaN);

                            if (imageUri != null) {
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference("imagenes-negocios" + UUID.randomUUID().toString());
                                storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        String urlImagen = uri.toString();
                                        updateMap.put("url_imagen_negocio", urlImagen);
                                        updateNegocioData(negocioId, updateMap);
                                    });
                                });
                            } else {
                                updateNegocioData(negocioId, updateMap);
                            }
                        } else {
                            Toast.makeText(ActualizarDatosNActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ActualizarDatosNActivity.this, "Error al verificar la colección Negocios", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateNegocioData(String negocioId, Map<String, Object> updateMap) {
        mFirestore.collection("Negocios").document(negocioId).update(updateMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ActualizarDatosNActivity.this, "Negocio actualizado con éxito!", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(ActualizarDatosNActivity.this, MantenedorNegocioActivity.class));
                })
                .addOnFailureListener(e -> Toast.makeText(ActualizarDatosNActivity.this, "Error al actualizar el negocio", Toast.LENGTH_SHORT).show());
    }
    private void getLugar(String id){
        mFirestore.collection("Negocios").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nameLugar = documentSnapshot.getString("Nombre");

                nombre.setText(nameLugar);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActualizarDatosNActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
