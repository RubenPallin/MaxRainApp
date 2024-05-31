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
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_CART_ITEMS = "CartItems";
    static final String KEY_IS_LOGGED_IN = "IsLoggedIn";
    static final String KEY_TOKEN = "Token";
    private RecyclerView recyclerView;
    private CarritoAdapter adapter;
    private List<ArticulosData> articulosCarrito;
    private View carritoVacio;
    private View carritoRegistrado;
    private View contentLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrito);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        Button btnInicioSesion = findViewById(R.id.buttonSession);
        Button btnEmpezarCompra = findViewById(R.id.buttonCompra);
        Button btnContinuar = findViewById(R.id.buttonContinuar);
        progressBar = findViewById(R.id.progressBar);
        contentLayout = findViewById(R.id.carritoLayout);
        carritoVacio = findViewById(R.id.carritoVacio);
        carritoRegistrado = findViewById(R.id.carritoRegistrado);

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

        // Verificar el estado de inicio de sesión y actualizar la interfaz en consecuencia
        actualizarInterfaz();

    }

    // Método para obtener los artículos del carrito desde el backend
    private void obtenerArticulosCarrito() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String sessionToken = sharedPreferences.getString(KEY_TOKEN, "");
        // URL del servidor para obtener los artículos del carrito (suponiendo que exista un endpoint /carrito)
        String url = "http://10.0.2.2:8000/carrito/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray itemsArray = response.getJSONArray("items");
                            // Limpiar la lista antes de agregar nuevos elementos
                            articulosCarrito.clear();
                            for (int i = 0; i < itemsArray.length(); i++) {
                                JSONObject itemObject = itemsArray.getJSONObject(i);
                                JSONObject articuloObject = itemObject.getJSONObject("articulo");

                                // Obtener los datos del artículo del carrito
                                String codigoArticulo = articuloObject.getString("codigo_articulo");
                                String nombre = articuloObject.getString("descripcion");
                                double precio = articuloObject.getDouble("precio");
                                String codigoFamilia = articuloObject.getString("familia_id");
                                String codigoMarca = articuloObject.getString("marca_id");
                                int imagen = R.drawable.imagen;
                                // Obtén la cantidad del artículo del carrito
                                int cantidad = itemObject.getInt("cantidad");

                                // Agregar el artículo y su cantidad al carrito
                                articulosCarrito.add(new ArticulosData(codigoArticulo, nombre, imagen , codigoFamilia, codigoMarca, precio));
                            }
                            // Notificar al adaptador que los datos han cambiado
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
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Agregar el token de sesión como encabezado de autorización
                Map<String, String> headers = new HashMap<>();
                headers.put("token", sessionToken);
                return headers;
            }
        };

        // Agregar la solicitud a la cola de solicitudes de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void actualizarInterfaz() {
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);

        if (isLoggedIn) {
            // Si el usuario está autenticado, muestra el diseño del carrito con artículos
            carritoVacio.setVisibility(View.GONE);
            carritoRegistrado.setVisibility(View.VISIBLE);
            // Obtener y mostrar los artículos del carrito
            obtenerArticulosCarrito();
        } else {
            // Si el usuario no está autenticado, muestra el diseño del carrito vacío
            carritoVacio.setVisibility(View.VISIBLE);
            carritoRegistrado.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE); // Ocultar el ProgressBar si el usuario no está logueado
            // No se llama a obtenerArticulosCarrito() aquí cuando el usuario no está autenticado
        }
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