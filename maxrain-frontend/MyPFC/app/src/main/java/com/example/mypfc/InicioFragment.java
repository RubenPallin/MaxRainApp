package com.example.mypfc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InicioFragment extends Fragment {


    public InicioFragment newInstance() {
        return new InicioFragment();
    }
    private RecyclerView recyclerView;
    private InicioAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_inicio);

        // Configurar el GridLayoutManager con orientación horizontal
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        int[] fotos = {
                R.drawable.hit,
                R.drawable.krain,
                R.drawable.max_rain_turf,
                R.drawable.solem,
                R.drawable.rainbirdd
        };

        adapter = new InicioAdapter(fotos, getContext());
        recyclerView.setAdapter(adapter);

        // Desplazarse a la posición inicial centrada
        recyclerView.scrollToPosition((fotos.length / 2) - 1);

        return view;
    }
}