package com.example.mypfc;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BusquedaFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArticulosAdapter adapter;
    private List<ArticulosData> articulosList;
    private BusquedaFragment main_context = this;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busqueda, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_busqueda);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        articulosList = new ArrayList<>();

        adapter = new ArticulosAdapter(articulosList, getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void actualizarResultados(List<ArticulosData> nuevosArticulos) {
        articulosList.clear();
        articulosList.addAll(nuevosArticulos);
        adapter.notifyDataSetChanged();
    }
}