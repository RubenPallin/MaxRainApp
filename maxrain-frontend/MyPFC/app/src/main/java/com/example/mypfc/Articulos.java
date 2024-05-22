package com.example.mypfc;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.View;
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

public class Articulos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArticulosAdapter adapter;
    private List<ArticulosData> articulosList;
    private static final String URL = "https://tuapi.com/articulos?codigo_familia=12345"; // Actualiza con tu URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulos);

        recyclerView = findViewById(R.id.recycler_view_articulos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        articulosList = new ArrayList<>();
        adapter = new ArticulosAdapter(articulosList, this);
        recyclerView.setAdapter(adapter);

        String codigoFamilia = getIntent().getStringExtra("codigo_familia");
        if (codigoFamilia != null) {
            obtenerArticulos(codigoFamilia);
        }
    }


    private void obtenerArticulos(String codigoFamilia) {
            String url = "http://10.0.2.2:8000/articulos/" + codigoFamilia;
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                articulosList.clear();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    ArticulosData articulo = new ArticulosData(
                                            jsonObject.getString("codigo_articulo"),
                                            jsonObject.getString("descripcion"),
                                            R.drawable.imagen,
                                            jsonObject.getString("codigo_familia"),
                                            jsonObject.getString("codigo_marca"),
                                            jsonObject.getDouble("precio")
                                    );
                                    articulosList.add(articulo);
                                }
                                adapter.notifyDataSetChanged();
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
                            Toast.makeText(Articulos.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonArrayRequest);
    }
}