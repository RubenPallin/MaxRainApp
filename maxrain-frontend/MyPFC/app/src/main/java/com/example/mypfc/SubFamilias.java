package com.example.mypfc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
    private List<MaxData> subfamiliasList = new ArrayList<>();
    private int codigoFamiliaPrincipal; // Cambio a int

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_familias);

        recyclerView = findViewById(R.id.recycler_view_subfamilias);
        progressBar = findViewById(R.id.progress_bar_subfamilias);

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
        // Esto es necesario porque en el backend, los códigos de familia se esperan con ceros iniciales.
        //Ya que en la base de datos figura así.
        // Por ejemplo, si el código de familia es 2, se formateará como "02".
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
                        try {
                            progressBar.setVisibility(View.INVISIBLE);
                            subfamiliasList.clear(); // Clear the list before adding new items
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                MaxData subfamilia = new MaxData(jsonObject);
                                subfamiliasList.add(subfamilia);
                            }
                            adapter = new SubFamiliasAdapter(subfamiliasList, SubFamilias.this);
                            recyclerView.setAdapter(adapter); // Configurar el adaptador y adjuntarlo al RecyclerView
                            adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
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
                        error.printStackTrace();
                        Toast.makeText(SubFamilias.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void obtenerSubfamiliasAdicionales(String codigoFamilia) {
        // No necesitamos formatear el código de familia porque ya debería estar en el formato correcto
        String url = "http://10.0.2.2:8000/subfamilias/" + codigoFamilia;
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
                            subfamiliasList.clear(); // Clear the list before adding new items
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                MaxData subfamilia = new MaxData(jsonObject);
                                subfamiliasList.add(subfamilia);
                            }
                            adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
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
                        error.printStackTrace();
                        Toast.makeText(SubFamilias.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}