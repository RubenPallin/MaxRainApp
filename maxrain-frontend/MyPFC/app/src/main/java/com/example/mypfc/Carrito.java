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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.protocol.HttpRequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carrito extends AppCompatActivity {
    private Context main_context = this;

    private SharedPreferences sharedPreferences;

    private List<ArticulosData> articulosCarrito;
    private View contentLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrito);


        Button btnInicioSesion = findViewById(R.id.buttonSession);
        Button btnEmpezarCompra = findViewById(R.id.buttonCompra);

        progressBar = findViewById(R.id.progressBar);
        contentLayout = findViewById(R.id.carritoLayout);


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

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Manejar el clic en el botón de retroceso
            Intent intent = new Intent(this, MainMaxRain.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}