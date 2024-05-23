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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulos);

        recyclerView = findViewById(R.id.recycler_view_articulos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        articulosList = new ArrayList<>();
        adapter = new ArticulosAdapter(articulosList, this);
        recyclerView.setAdapter(adapter);

        String articulosJson = getIntent().getStringExtra("articulos");
        if (articulosJson != null) {
            try {
                JSONArray articulosArray = new JSONArray(articulosJson);
                for (int i = 0; i < articulosArray.length(); i++) {
                    JSONObject jsonObject = articulosArray.getJSONObject(i);
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
    }
}