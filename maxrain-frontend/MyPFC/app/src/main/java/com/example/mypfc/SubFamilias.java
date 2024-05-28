package com.example.mypfc;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
    private int codigoFamiliaPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_familias);

        recyclerView = findViewById(R.id.recycler_view_subfamilias);
        progressBar = findViewById(R.id.progress_bar_subfamilias);
        toolbarSub = findViewById(R.id.toolbar_subfamilias);

        setSupportActionBar(toolbarSub);

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

        String url = "http://10.0.2.2:8000/subfamilias/" + codigoFamiliaFormateado + "/";

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
                            //obtenerArticulos(codigoFamiliaFormateado);  // Llamar a la función para obtener los artículos
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
                        error.printStackTrace();
                        Toast.makeText(SubFamilias.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                    }
                }
        );

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