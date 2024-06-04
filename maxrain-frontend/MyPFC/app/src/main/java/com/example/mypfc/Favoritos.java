package com.example.mypfc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Favoritos extends AppCompatActivity {

    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_CART_ITEMS = "CartItems";
    static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    static final String KEY_TOKEN = "token";
    private List<ArticulosData> articulosFavoritos;
    private ArticulosAdapter adapter;
    private RecyclerView recyclerView;
    private Context main_context2 = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favoritos);

        // Inicializar la lista de artículos del carrito
        articulosFavoritos = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_favoritos);

        // Crear e instanciar el adapter
        adapter = new ArticulosAdapter(articulosFavoritos, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbarFav = findViewById(R.id.toolbar_favoritos);
        setSupportActionBar(toolbarFav);

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

        get_favoritos();
    }

    private void get_favoritos() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String sessionToken = sharedPreferences.getString(KEY_TOKEN, "");

        String url = "http://10.0.2.2:8000/favoritos/";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("JSON_RESPONSE", response.toString()); // Agrega esta línea para imprimir el JSON recibido

                            // Limpiar la lista antes de agregar nuevos elementos
                            articulosFavoritos.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject articuloObject = response.getJSONObject(i);

                                // Obtener los datos del artículo favorito
                                String codigoArticulo = articuloObject.getString("codigo_articulo");
                                String nombre = articuloObject.getString("descripcion");
                                double precio = articuloObject.getDouble("precio");
                                String codigoFamilia = articuloObject.getString("familia");
                                String codigoMarca = articuloObject.getString("marca");
                                int imagen = R.drawable.imagen;

                                // Agregar el artículo a la lista de favoritos
                                articulosFavoritos.add(new ArticulosData(codigoArticulo, nombre, imagen, codigoFamilia, codigoMarca, precio));
                            }
                            // Notificar al adaptador que los datos han cambiado
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Favoritos.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(Favoritos.this, "Error al obtener los artículos favoritos", Toast.LENGTH_SHORT).show();
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
        requestQueue.add(jsonArrayRequest);
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