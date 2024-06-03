package com.example.mypfc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetalleArticulo extends AppCompatActivity {
    private ImageView imagenMax;
    private TextView nombreMax;
    private TextView precioMax;
    private TextView precio;
    private TextView marcaMax;
    private TextView marca;
    private ImageButton botonLike;
    private Button botonCarrito;
    private Toolbar toolbarDet;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_CART_ITEMS = "CartItems";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_articulo);

        imagenMax = findViewById(R.id.imagen_max);
        nombreMax = findViewById(R.id.nombre_max);
        precioMax = findViewById(R.id.precio_max);
        precio = findViewById(R.id.precio);
        marcaMax = findViewById(R.id.marca_max);
        marca = findViewById(R.id.marca);
        botonLike = findViewById(R.id.boton_like);
        botonCarrito = findViewById(R.id.boton_carrito);
        toolbarDet = findViewById(R.id.toolbar_detalle_art);
        setSupportActionBar(toolbarDet);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Cambiar el color de la flecha de retroceso
            final Drawable upArrow = ContextCompat.getDrawable(this, com.google.android.material.R.drawable.abc_ic_ab_back_material);
            if (upArrow != null) {
                upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_ATOP);
                actionBar.setHomeAsUpIndicator(upArrow);
            }
        }

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        String precioTexto = intent.getStringExtra("precio");
        String codigoMarca = intent.getStringExtra("marca");
        int imagenUrl = intent.getIntExtra("imagen", R.drawable.imagen);

        String codigoArticulo = getIntent().getStringExtra("codigo_articulo");

        nombreMax.setText(nombre);
        precio.setText(precioTexto);
        marca.setText(codigoMarca);

        Glide.with(this)
                .load(imagenUrl)
                .into(imagenMax);

        botonLike.setOnClickListener(v -> {

        });

        botonCarrito.setOnClickListener(v -> añadirArticuloAlCarrito(codigoArticulo));
    }
    private void añadirArticuloAlCarrito(String codigoArticulo) {
        // Obtener el token de sesión guardado localmente
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String sessionToken = sharedPreferences.getString("token", "");

        Log.d("SessionToken", sessionToken);

        if (sessionToken == null || sessionToken.isEmpty()) {
            Toast.makeText(this, "No se encontró el token de sesión", Toast.LENGTH_SHORT).show();
            return; // Detener la ejecución si no hay token
        }

        // Crear un objeto JSON con los datos del artículo
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("codigo_articulo", codigoArticulo);
            jsonObject.put("cantidad", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear una solicitud POST a tu nuevo endpoint de carrito
        String url = "http://10.0.2.2:8000/carrito/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    // Manejar la respuesta del servidor
                    Toast.makeText(this, "Artículo añadido al carrito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, CarritoRegistrado.class);
                    startActivity(intent);
                },
                error -> {
                    Toast.makeText(this, "Error al añadir el artículo al carrito", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("token", sessionToken);
                return headers;
            }
        };

        // Agregar la solicitud a la cola de solicitudes de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
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
