package com.example.mypfc;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MaxAdapter extends RecyclerView.Adapter<MaxViewHolder> {

    private List<MaxData> allElements;
    private Activity activity;

    // Constructor que acepta un listener
    public MaxAdapter(List<MaxData> allElements, Activity activity) {
        this.allElements = allElements != null ? allElements : new ArrayList<>();
        this.activity = activity;
    }

    @NonNull
    @Override
    public MaxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla la vista del ViewHolder desde el diseño activity_max_view_holder.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_max_view_holder, parent, false);
        // Retorna un nuevo objeto ProtagonistasViewHolder con la vista inflada
        return new MaxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaxViewHolder holder, int position) {
        // Obtener los datos de la familia en la posición especificada
        MaxData data = allElements.get(position);
        // Mostrar los datos en el ViewHolder
        holder.bindMaxMethod(data, activity);
    }

    @Override
    public int getItemCount() {
        return allElements.size();
    }
}