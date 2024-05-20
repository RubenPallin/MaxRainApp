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
        //progressBar = rootView.findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        listaFamilias = new ArrayList<>();

        adapter = new MaxAdapter(listaFamilias, getActivity());
        recyclerView.setAdapter(adapter);

        obtenerFamilias();

        return rootView;
    }

    private void obtenerFamilias() {
        String url = "http://10.0.2.2:8000/familias/";
        //progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //progressBar.setVisibility(View.INVISIBLE);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String codigoFamilia = jsonObject.getString("codigo_familia");
                                if (codigoFamilia.matches("^(0[1-9]|1[0-2])$")) {
                                    MaxData familia = new MaxData(jsonObject);
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
                        //progressBar.setVisibility(View.INVISIBLE);
                        error.printStackTrace();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }
}
