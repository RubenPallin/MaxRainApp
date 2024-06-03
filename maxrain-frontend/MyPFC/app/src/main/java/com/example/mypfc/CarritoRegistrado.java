package com.example.mypfc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarritoRegistrado extends AppCompatActivity {
    private Context main_context = this;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_CART_ITEMS = "CartItems";
    static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    static final String KEY_TOKEN = "token";
    private RecyclerView recyclerView;
    private CarritoAdapter adapter;
    private List<ArticulosData> articulosCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrito_registrado);
        Button btnContinuar = findViewById(R.id.boton_continuar);

        // Inicializar la lista de artículos del carrito
        articulosCarrito = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_carrito);

        // Crear e instanciar el adapter
        adapter = new CarritoAdapter(articulosCarrito, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbarCar = findViewById(R.id.toolbar_carrito_registrado);
        setSupportActionBar(toolbarCar);

        // Habilitar el botón de regreso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.x);


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

        obtenerArticulosCarrito();
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
                            Log.d("JSON_RESPONSE", response.toString()); // Agrega esta línea para imprimir el JSON recibido

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
                                String codigoFamilia = articuloObject.getString("familia");
                                String codigoMarca = articuloObject.getString("marca");
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
                            Toast.makeText(CarritoRegistrado.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(CarritoRegistrado.this, "Error al obtener los artículos del carrito", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CarritoRegistrado.this, "Error al eliminar el artículo del carrito", Toast.LENGTH_SHORT).show();
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
            String codigoFamilia = jsonObject.getString("familia");
            String codigoMarca = jsonObject.getString("marca");
            double precio = jsonObject.getDouble("precio");

            return new ArticulosData(codigoArticulo, nombre, imageURL, codigoFamilia, codigoMarca, precio);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
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