package com.example.mypfc;

import static java.security.AccessController.getContext;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

public class Articulos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArticulosAdapter adapter;
    private List<ArticulosData> articulosList;
    private ImageView imagennoDisp;
    private TextView textnoDisp;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulos);
        imagennoDisp = findViewById(R.id.imagen_no_disp);
        textnoDisp = findViewById(R.id.texto_no_disp);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        Toolbar toolbarArt = findViewById(R.id.toolbar_articulos);
        setSupportActionBar(toolbarArt);

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

        recyclerView = findViewById(R.id.recycler_view_articulos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        articulosList = new ArrayList<>();
        adapter = new ArticulosAdapter(articulosList, this);
        recyclerView.setAdapter(adapter);

        String codigoFamilia = getIntent().getStringExtra("codigo_familia");
        if (!codigoFamilia.isEmpty()) {
            getArticulos(codigoFamilia);
        } else {
            mostrarNoData();
        }
    }

    private void getArticulos(String codigoFamilia) {
        // URL del servidor para obtener los artículos
        String url = "http://10.0.2.2:8000/articulos/" + codigoFamilia + "/";

        // Crear la solicitud GET
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            articulosList.clear();  // Limpia la lista antes de agregar nuevos elementos
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                ArticulosData articulo = new ArticulosData(jsonObject);
                                articulosList.add(articulo);
                            }
                            adapter.notifyDataSetChanged();
                            if (articulosList.isEmpty()) {
                                mostrarNoData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Articulos.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        mostrarNoData();
                    }
                }
        );

        // Obtener la instancia de Volley y agregar la solicitud a la cola de solicitudes
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
    private void mostrarNoData() {
        imagennoDisp.setVisibility(View.VISIBLE);
        textnoDisp.setVisibility(View.VISIBLE);
    }
}