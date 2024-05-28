package com.example.mypfc;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class SegundasSubFamilias extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ArrayList<MaxData> segundasSubfamiliasList;
    private SegundasSubfamiliasAdapter adapter;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segundas_sub_familias);


        Toolbar toolbarsf = findViewById(R.id.toolbar_segundassubfamilias);
        setSupportActionBar(toolbarsf);

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

        recyclerView = findViewById(R.id.recycler_view_segundas_subfamilias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        segundasSubfamiliasList = new ArrayList<>();
        adapter = new SegundasSubfamiliasAdapter(segundasSubfamiliasList, this);
        recyclerView.setAdapter(adapter);

        progressBar = findViewById(R.id.progress_bar_subfamilias);
        progressBar.setVisibility(View.VISIBLE); // Mostrar la ProgressBar al iniciar la actividad

        // Obtener el código de la subfamilia desde el intent
        String codigoSubfamilia = getIntent().getStringExtra("codigo_subfamilia");
        if (codigoSubfamilia != null && !codigoSubfamilia.isEmpty()) {
            obtenerSegundasSubfamilias(codigoSubfamilia);
        } else {
            Toast.makeText(SegundasSubFamilias.this, "Error al obtener el codigo", Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerSegundasSubfamilias(String codigoSubfamilia) {
        String url = "http://10.0.2.2:8000/subfamilias/" + codigoSubfamilia + "/";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE); // Ocultar la ProgressBar cuando se obtienen los datos
                        try {
                            segundasSubfamiliasList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                MaxData subfamilia = new MaxData(jsonObject);
                                segundasSubfamiliasList.add(subfamilia);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SegundasSubFamilias.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE); // Ocultar la ProgressBar en caso de error
                        error.printStackTrace();
                        Toast.makeText(SegundasSubFamilias.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Manejar el clic en el botón de retroceso
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}