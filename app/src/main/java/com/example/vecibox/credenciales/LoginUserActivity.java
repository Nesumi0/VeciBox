package com.example.vecibox.credenciales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vecibox.Negocio.MantenedorNegocioActivity;
import com.example.vecibox.R;
import com.example.vecibox.Usuario.MenuActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class LoginUserActivity extends AppCompatActivity {
    Button btn_ingresar, btn_reg2;
    EditText email,password;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        btn_ingresar = findViewById(R.id.btn_ingresar);
        btn_reg2 = findViewById(R.id.btn_reg2);
        email = findViewById(R.id.emailLog);
        password = findViewById(R.id.passLog);
        mAuth = FirebaseAuth.getInstance();

        btn_reg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginUserActivity.this,RegistroUserActivity.class));
            }
        });

        btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUser = email.getText().toString().trim();
                String passUser = password.getText().toString().trim();
                 if(emailUser.isEmpty() || passUser.isEmpty()){
                     Toast.makeText(LoginUserActivity.this, "Porfavor Ingresar Datos", Toast.LENGTH_SHORT).show();
                 }else {
                     loginUser(emailUser,passUser);
                 }
            }
        });
    }

    private void loginUser(String emailUser, String passUser) {
        mAuth.signInWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userEmail = mAuth.getCurrentUser().getEmail();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference negociosCollection = db.collection("Negocios");

                        negociosCollection.whereEqualTo("gmail", userEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (!task.getResult().isEmpty()) {
                                    finish();
                                    startActivity(new Intent(LoginUserActivity.this, MantenedorNegocioActivity.class));
                                } else {
                                    finish();
                                    startActivity(new Intent(LoginUserActivity.this, MenuActivity.class));
                                }
                            } else {
                                Toast.makeText(LoginUserActivity.this, "Error al verificar la colección", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginUserActivity.this, "Este usuario no se encuentra registrado", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginUserActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
            }
        });
    }


}