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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.protocol.HttpRequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Carrito extends AppCompatActivity {

    private Context main_context = this;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_CART_ITEMS = "CartItems";
    private static final String KEY_IS_LOGGED_IN = "IsLoggedIn";
    private RecyclerView recyclerView;
    private CarritoAdapter adapter;
    private List<ArticulosData> articulosCarrito;

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

        // Inicializar la lista de artículos del carrito
        articulosCarrito = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_carrito);

        // Crear e instanciar el adapter
        adapter = new CarritoAdapter(articulosCarrito, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        obtenerArticulosCarrito();

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

    // Método para obtener los artículos del carrito desde el backend
    private void obtenerArticulosCarrito() {
        // URL del servidor para obtener los artículos del carrito (suponiendo que exista un endpoint /carrito)
        String url = "http://10.0.2.2:8000/carrito/";

        // Crear la solicitud GET
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            articulosCarrito.clear();  // Limpiar la lista antes de agregar nuevos elementos
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                // Suponiendo que tienes un método para crear un objeto ArticuloCarrito a partir de un JSONObject
                                ArticulosData articulo = crearArticuloDesdeJSON(jsonObject);
                                articulosCarrito.add(articulo);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Carrito.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(Carrito.this, "Error al obtener los artículos del carrito", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Obtener la instancia de Volley y agregar la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    // Método para eliminar un artículo del carrito
    private void eliminarArticuloDelCarrito(String idArticulo) {
        // URL del servidor para eliminar el artículo del carrito (suponiendo que exista un endpoint /carrito/{idArticulo})
        String url = "http://tu-backend.com/api/carrito/" + idArticulo;

        // Crear la solicitud DELETE
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Manejar la respuesta (puede ser vacía si la eliminación fue exitosa)
                        // Actualizar la interfaz de usuario si es necesario
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(Carrito.this, "Error al eliminar el artículo del carrito", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Obtener la instancia de Volley y agregar la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private ArticulosData crearArticuloDesdeJSON(JSONObject jsonObject) {
        try {
            String codigoArticulo = jsonObject.getString("codigo_articulo");
            String nombre = jsonObject.getString("nombre");
            int imageURL = jsonObject.getInt("imagen_url");
            String codigoFamilia = jsonObject.getString("familia_id");
            String codigoMarca = jsonObject.getString("marca_id");
            double precio = jsonObject.getDouble("precio");

            return new ArticulosData(codigoArticulo, nombre, imageURL, codigoFamilia, codigoMarca, precio);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
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