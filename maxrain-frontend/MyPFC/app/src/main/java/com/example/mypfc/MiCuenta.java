package com.example.mypfc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MiCuenta extends AppCompatActivity {

    private Context main_context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mi_cuenta);

        Button btnInicio = findViewById(R.id.button_inicio);
        Button btnContacto = findViewById(R.id.button_contacto);
        Button btnOpinion = findViewById(R.id.button_opinion);
        Button btnAjustes = findViewById(R.id.button_ajustes);
        Button btnRegister = findViewById(R.id.button_register);




        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_context, Login.class);
                startActivity(intent);
            }
        });

        btnContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Intent intent = new Intent(main_context, );
            //    startActivity(intent)
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_context, Register.class);
                startActivity(intent);
            }
        });

        btnOpinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Intent intent = new Intent(main_context, Login.class);
            //    startActivity(intent);
            }
        });

    }
}