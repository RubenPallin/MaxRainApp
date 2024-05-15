package com.example.mypfc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ProductosFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<MaxData> listaFamilias;
    private MaxAdapter adapter;

    public ProductosFragment newInstance() {
        return new ProductosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_productos, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        listaFamilias = new ArrayList<>();
        adapter = new MaxAdapter(listaFamilias, getActivity(), new MaxAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MaxData familia) {
                // Lógica para manejar el clic en la familia
                // Por ejemplo, obtener las subfamilias de la familia principal y actualizar el RecyclerView
                obtenerSubfamiliasDeLaFamiliaPrincipal(familia);
            }
        });
        recyclerView.setAdapter(adapter);

        obtenerFamilias();

        return rootView;
    }


    private void obtenerFamilias() {
        String url = "http://10.0.2.2:8000/familias/";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String codigoFamilia = jsonObject.getString("codigo_familia");
                                if (codigoFamilia.matches("^(0[1-9]|1[0-2])$")) {
                                    MaxData familia = new MaxData(jsonObject); // Aquí asignamos la ID del recurso drawable
                                    listaFamilias.add(familia);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }
    private void obtenerSubfamiliasDeLaFamiliaPrincipal(MaxData familia) {
        String url = "http://10.0.2.2:8000/subfamilias/" + familia.getCodigoFamilia() + "/";

        // Crear una solicitud JSON array utilizando Volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Limpiar la lista actual de subfamilias
                            familia.getSubfamilies().clear();

                            // Agregar las subfamilias obtenidas del servidor a la lista
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                MaxData subfamilia = new MaxData(
                                        jsonObject.getString("descripcion_familia"),
                                        R.drawable.imagen,
                                        jsonObject.getInt("codigo_familia")
                                );
                                familia.addSubfamily(subfamilia);
                            }

                            // Notificar al adaptador que los datos han cambiado
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        // Agregar la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(getActivity()).add(jsonArrayRequest);
    }
}
