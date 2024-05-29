package com.example.mypfc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Carrito extends AppCompatActivity {

    private Context main_context = this;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_CART_ITEMS = "CartItems";
    private static final String KEY_IS_LOGGED_IN = "IsLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrito);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        Button btnInicioSesion = findViewById(R.id.buttonSession);
        Button btnEmpezarCompra = findViewById(R.id.buttonCompra);
        Button btnContinuar = findViewById(R.id.buttonContinuar);
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

        // Inicializar el carrito
        updateCartUI();

        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_context, Login.class);
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

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
                if (isLoggedIn) {
                    Toast.makeText(main_context, "Producto comprado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(main_context, Login.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void updateCartUI() {
        String cartItems = sharedPreferences.getString(KEY_CART_ITEMS, null);
        if (cartItems == null) {
            // Mostrar UI de carrito vacío
            findViewById(R.id.carritoVacio).setVisibility(View.VISIBLE);
            findViewById(R.id.carritoRegistrado).setVisibility(View.GONE);
        } else {
            // Mostrar UI de carrito lleno
            findViewById(R.id.carritoVacio).setVisibility(View.GONE);
            findViewById(R.id.carritoRegistrado).setVisibility(View.VISIBLE);
        }
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