package com.example.vecibox.Negocio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vecibox.R;
import com.example.vecibox.Usuario.MenuActivity;
import com.example.vecibox.credenciales.LoginUserActivity;
import com.google.firebase.auth.FirebaseAuth;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MantenedorNegocioActivity extends AppCompatActivity {
    Button btn_veci, btn_logout, btn_update, btn_coments;
    ImageView imageViewNegocio;
    TextView txt_NombreNegocio, txt_DescripcionNegocio, txt_Horarios, txt_Operaciones, txt_Gmail;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenedor_negocio);

        btn_update = findViewById(R.id.btn_update);
        btn_logout = findViewById(R.id.btn_logout);
        btn_veci = findViewById(R.id.btn_veci);
        btn_coments = findViewById(R.id.button3);
        imageViewNegocio = findViewById(R.id.imageViewNegocio);
        txt_NombreNegocio = findViewById(R.id.txt_nameN);
        txt_DescripcionNegocio = findViewById(R.id.txt_DescN);
        txt_Horarios = findViewById(R.id.txt_HorariosN);
        txt_Operaciones = findViewById(R.id.txt_OperacionesN);
        txt_Gmail = findViewById(R.id.txt_gmailN);
        mAuth = FirebaseAuth.getInstance();

        btn_logout.setOnClickListener(v -> logout());

        btn_veci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MantenedorNegocioActivity.this, MenuActivity.class));
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MantenedorNegocioActivity.this,ActualizarDatosNActivity.class));
            }
        });
        obtenerInformacionNegocio();
        btn_coments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MantenedorNegocioActivity.this, ComentariosNegocioActivity.class));
            }
        });
    }


    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginUserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void obtenerInformacionNegocio() {
        String gmail = mAuth.getCurrentUser().getEmail();
        db.collection("Negocios").whereEqualTo("gmail", gmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String nombreNegocio = document.getString("Nombre");
                    String descripcionNegocio = document.getString("Descripcion");
                    String horariosNegocio = document.getString("Horario");
                    String operacionesNegocio = document.getString("Operaciones");
                    String gmailNegocio = document.getString("gmail");
                    String urlDeLaImagen = document.getString("url_imagen_negocio");

                    txt_NombreNegocio.setText("Nombre: "+nombreNegocio);
                    txt_DescripcionNegocio.setText("Descripcion: "+descripcionNegocio);
                    txt_Horarios.setText("Horario: "+horariosNegocio);
                    txt_Operaciones.setText("Operaciones: "+operacionesNegocio);
                    txt_Gmail.setText("gmail: "+gmailNegocio);

                    Glide.with(this)
                            .load(urlDeLaImagen)
                            .apply(RequestOptions.placeholderOf(R.drawable.baseline_upload_24))
                            .into(imageViewNegocio);
                }
            } else {
                Toast.makeText(this, "Error al obtener información del negocio", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
