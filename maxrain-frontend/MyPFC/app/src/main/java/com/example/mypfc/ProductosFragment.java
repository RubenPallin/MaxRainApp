package com.example.mypfc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProductosFragment extends Fragment {

    private RecyclerView recyclerView;

    public ProductosFragment newInstance() {
        return new ProductosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_productos, container, false);

        // Encuentra el RecyclerView en la vista inflada
        recyclerView = rootView.findViewById(R.id.recycler_view); // Reemplaza "myRecyclerView" con el ID real de tu RecyclerView


        return rootView;


    }
}