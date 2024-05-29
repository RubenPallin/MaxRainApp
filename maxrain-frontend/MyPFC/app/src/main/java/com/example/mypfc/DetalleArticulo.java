package com.example.mypfc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;

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

        nombreMax.setText(nombre);
        precio.setText(precioTexto);
        marca.setText(codigoMarca);

        Glide.with(this)
                .load(imagenUrl)
                .into(imagenMax);

        botonLike.setOnClickListener(v -> {

        });

        botonCarrito.setOnClickListener(v -> añadirCarrito(nombre, precioTexto, codigoMarca, imagenUrl));
    }
    private void añadirCarrito(String nombre, String precio, String marca, int imagen) {
        // Añadir artículo al carrito en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String cartItem = nombre + ";" + precio + ";" + marca + ";" + imagen;
        editor.putString(KEY_CART_ITEMS, cartItem);
        editor.apply();

        Toast.makeText(this, "Artículo añadido al carrito", Toast.LENGTH_SHORT).show();

        // Redirigir a la pantalla del carrito
        Intent intent = new Intent(this, Carrito.class);
        startActivity(intent);
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
