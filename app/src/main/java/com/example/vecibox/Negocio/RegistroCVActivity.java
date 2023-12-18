package com.example.vecibox.Negocio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vecibox.R;
import com.example.vecibox.credenciales.LoginUserActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegistroCVActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    EditText nombre, informacion, gmail, operacion, horarios;
    ImageView fotonegocio;
    Button btn_add;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cvactivity);

        mAuth = FirebaseAuth.getInstance();
        nombre = findViewById(R.id.nombreN);
        informacion = findViewById(R.id.InfoN);
        gmail = findViewById(R.id.gmailN);
        operacion = findViewById(R.id.OpeN);
        horarios = findViewById(R.id.HorarioN);
        fotonegocio = findViewById(R.id.imageN);
        btn_add = findViewById(R.id.btn_añadir);

        mFirestore = FirebaseFirestore.getInstance();

        fotonegocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameN = nombre.getText().toString().trim();
                String infoN = informacion.getText().toString().trim();
                String gmailN = gmail.getText().toString().trim();
                String operaN = operacion.getText().toString().trim();
                String horarioN = horarios.getText().toString().trim();

                if (nameN.isEmpty() && infoN.isEmpty() && gmailN.isEmpty() && operaN.isEmpty() && horarioN.isEmpty()) {
                    Toast.makeText(RegistroCVActivity.this, "Ingresar los datos", Toast.LENGTH_SHORT).show();
                }
                else {
                    postNegocio(nameN, infoN, horarioN, operaN, gmailN);
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

    private void postNegocio(String nameN, String infoN,String horarioN ,String operaN,String gmailN) {

        String userId = mAuth.getCurrentUser().getUid();
        String negocioId = UUID.randomUUID().toString();


        FirebaseFirestore.getInstance().collection("Negocios")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            Toast.makeText(RegistroCVActivity.this, "Este usuario ya tiene un negocio registrado.", Toast.LENGTH_SHORT).show();
                        } else {
                            Map<String, Object> map = new HashMap<>();
                            map.put("Nombre", nameN);
                            map.put("Descripcion", infoN);
                            map.put("Horario", horarioN);
                            map.put("Operaciones", operaN);
                            map.put("gmail",gmailN);
                            map.put("userId", userId);
                            map.put("id", negocioId);

                            if (imageUri != null) {
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference("imagenes-negocios" + UUID.randomUUID().toString());
                                storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        String urlImagen = uri.toString();
                                        map.put("url_imagen_negocio", urlImagen);
                                        saveNegocioData(map);
                                    });
                                });
                            }
                        }
                    } else {
                        Toast.makeText(RegistroCVActivity.this, "Error al verificar la colección Negocios", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveNegocioData(Map<String, Object> map) {
        mFirestore.collection("Negocios").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(RegistroCVActivity.this, "Negocio Ingresado con éxito!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(RegistroCVActivity.this, LoginUserActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroCVActivity.this, "Error al añadir negocio", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
