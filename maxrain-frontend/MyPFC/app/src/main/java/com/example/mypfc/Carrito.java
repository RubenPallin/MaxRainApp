package com.example.mypfc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Carrito extends AppCompatActivity {

    private Context main_context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrito);

        Button btnInicioSesion = findViewById(R.id.buttonSession);
        Button btnEmpezarCompra = findViewById(R.id.buttonCompra);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        View contentLayout = findViewById(R.id.carritoLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Habilitar el botón de regreso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.x);

        // Ocultar el contenido del carrito hasta que esté listo para mostrarse
        contentLayout.setVisibility(View.INVISIBLE);

        // Mostrar el ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        // Simular un retraso de 2 segundos antes de mostrar el contenido del carrito
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Ocultar el ProgressBar
                progressBar.setVisibility(View.INVISIBLE);

                // Mostrar el contenido del carrito
                contentLayout.setVisibility(View.VISIBLE);
            }
        }, 2000); // Retraso de 2 segundos


        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( main_context, Login.class);
                startActivity(intent);
            }
        });

        btnEmpezarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FamiliasFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}