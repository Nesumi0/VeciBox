package com.example.vecibox.Negocio;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vecibox.R;
import com.example.vecibox.Usuario.MenuActivity;

public class PantallaCVActivity extends AppCompatActivity {
    Button btn_seguir, btn_volver;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_cvactivity);

        btn_seguir = findViewById(R.id.btn_seguir);
        btn_volver = findViewById(R.id.btn_devolverse);

        btn_seguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PantallaCVActivity.this, RegistroCVActivity.class));
            }
        });

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PantallaCVActivity.this, MenuActivity.class));
            }
        });
    }
}