package com.example.mypfc;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

public class SubFamilias extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SubFamiliasAdapter adapter;
    private Toolbar toolbarSub;
    private List<MaxData> subfamiliasList = new ArrayList<>();
    private int codigoFamiliaPrincipal; // Cambio a int

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_familias);

        recyclerView = findViewById(R.id.recycler_view_subfamilias);
        progressBar = findViewById(R.id.progress_bar_subfamilias);
        toolbarSub = findViewById(R.id.toolbar_subfamilias);

        setSupportActionBar(toolbarSub);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.flecha2);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        if (intent != null) {
            codigoFamiliaPrincipal = intent.getIntExtra("codigo_familia", 0);
            if (codigoFamiliaPrincipal != 0) {
                obtenerPrimerasSubfamilias(codigoFamiliaPrincipal);
            } else {
                Toast.makeText(this, "Error al obtener el código de familia principal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void obtenerPrimerasSubfamilias(int codigoFamiliaPrincipal) {
        String codigoFamiliaFormateado = String.format("%02d", codigoFamiliaPrincipal);
        String url = "http://10.0.2.2:8000/subfamilias/" + codigoFamiliaFormateado;
        progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (response.length() == 0) {
                            obtenerArticulos(codigoFamiliaFormateado);  // Llamar a la función para obtener artículos
                        } else {
                            try {
                                subfamiliasList.clear();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    MaxData subfamilia = new MaxData(jsonObject);
                                    subfamiliasList.add(subfamilia);
                                }
                                adapter = new SubFamiliasAdapter(subfamiliasList, SubFamilias.this);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(SubFamilias.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                            // La subfamilia no tiene más subfamilias, obtener los artículos directamente
                            obtenerArticulos(codigoFamiliaFormateado);
                        } else {
                            error.printStackTrace();
                            Toast.makeText(SubFamilias.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void obtenerSubfamiliasAdicionales(final String codigoFamilia) {
        // Almacenar el codigoFamilia en una variable final
        final String codigoFamiliaFinal = codigoFamilia;

        String url = "http://10.0.2.2:8000/subfamilias/" + codigoFamiliaFinal;
        progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            progressBar.setVisibility(View.INVISIBLE);
                            subfamiliasList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                MaxData subfamilia = new MaxData(jsonObject);
                                subfamiliasList.add(subfamilia);
                            }
                            if (subfamiliasList.isEmpty()) {
                                // Si no hay subfamilias adicionales, cargar los productos
                                obtenerArticulos(codigoFamiliaFinal); // Utilizar codigoFamiliaFinal
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                            Toast.makeText(SubFamilias.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                            // La subfamilia no tiene más subfamilias, obtener los artículos directamente
                            obtenerArticulos(codigoFamiliaFinal); // Utilizar codigoFamiliaFinal
                        } else {
                            error.printStackTrace();
                            Toast.makeText(SubFamilias.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void obtenerArticulos(String codigoFamilia) {
        String url = "http://10.0.2.2:8000/articulos/" + codigoFamilia;
        progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (response.length() == 0) {
                            Toast.makeText(SubFamilias.this, "No se encontraron artículos.", Toast.LENGTH_SHORT).show();
                        } else {
                            List<ArticulosData> articulosList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    ArticulosData articulo = new ArticulosData(
                                            jsonObject.getString("codigo_articulo"),
                                            jsonObject.getString("descripcion"),
                                            R.drawable.imagen,  // Aquí deberías cargar la imagen correctamente
                                            jsonObject.getString("codigo_familia"),
                                            jsonObject.getString("codigo_marca"),
                                            jsonObject.getDouble("precio")
                                    );
                                    articulosList.add(articulo);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            // Pasar los datos de los artículos a la siguiente actividad
                            Intent intent = new Intent(SubFamilias.this, Articulos.class);
                            intent.putParcelableArrayListExtra("articulos", new ArrayList<>(articulosList));
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        error.printStackTrace();
                        Toast.makeText(SubFamilias.this, "Error al obtener los artículos", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

}