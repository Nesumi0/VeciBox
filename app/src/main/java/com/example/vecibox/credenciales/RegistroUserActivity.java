package com.example.vecibox.credenciales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vecibox.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegistroUserActivity extends AppCompatActivity {
    EditText name, email, pass, passConfirm;
    Button btn_reg;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_user);

        name = findViewById(R.id.nameReg);
        email = findViewById(R.id.emailReg);
        pass = findViewById(R.id.passReg);
        passConfirm = findViewById(R.id.passConfirmReg);
        btn_reg = findViewById(R.id.btn_registro);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameUser = name.getText().toString().trim();
                String emailUser = email.getText().toString().trim();
                String passUser = pass.getText().toString().trim();
                String passConUser = passConfirm.getText().toString().trim();

                if (!passUser.equals(passConUser)) {
                    Toast.makeText(RegistroUserActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                } else if (nameUser.isEmpty() || emailUser.isEmpty() || passUser.isEmpty() || passConUser.isEmpty()) {
                    Toast.makeText(RegistroUserActivity.this, "Por favor, rellenar todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(nameUser, emailUser, passUser);
                }
            }
        });
    }

    private void registerUser(String nameUser, String emailUser, String passUser) {
        mFirestore.collection("Usuarios").whereEqualTo("gmail", emailUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null && !task.getResult().isEmpty()) {
                        Toast.makeText(RegistroUserActivity.this, "¡Este correo ya se encuentra en uso!", Toast.LENGTH_SHORT).show();
                    } else {
                        mAuth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String userId = mAuth.getCurrentUser().getUid();

                                    Map<String, Object> user = new HashMap<>();
                                    user.put("name", nameUser);
                                    user.put("gmail", emailUser);
                                    user.put("password",passUser);
                                    user.put("userID",userId);
                                    mFirestore.collection("Usuarios").document(userId)
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(RegistroUserActivity.this, "Usuario registrado con éxito!", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    startActivity(new Intent(RegistroUserActivity.this, LoginUserActivity.class));
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(RegistroUserActivity.this, "Error al guardar en Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    mAuth.getCurrentUser().delete();
                                                }
                                            });

                                } else {
                                    Toast.makeText(RegistroUserActivity.this, "Error al registrar ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(RegistroUserActivity.this, "Error al verificar el correo electrónico", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
