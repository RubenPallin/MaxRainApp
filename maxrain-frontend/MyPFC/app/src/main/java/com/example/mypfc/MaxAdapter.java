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

import java.util.List;

public class MaxAdapter extends RecyclerView.Adapter<MaxViewHolder> {

    private List<MaxData> allElements;
    private Activity activity;

    private OnItemClickListener listener;

    // Constructor que acepta un listener
    public MaxAdapter(List<MaxData> allElements, Activity activity, OnItemClickListener listener) {
        this.allElements = allElements;
        this.activity = activity;
        this.listener = listener;
    }

    // Interfaz para manejar los clics en los elementos del RecyclerView
    public interface OnItemClickListener {
        void onItemClick(MaxData familia);
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

        // Guardar la posición final
        final int currentPosition = position;

        // Manejar el clic en la celda del RecyclerView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener la posición actual del adaptador
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Obtener los datos de la familia en la posición actual
                    MaxData clickedData = allElements.get(adapterPosition);

                    // Verificar si hay subfamilias asociadas con la familia actual
                    List<MaxData> subfamilies = clickedData.getSubfamilies();
                    if (subfamilies != null && !subfamilies.isEmpty()) {
                        // Si hay subfamilias, agregarlas al conjunto de datos y notificar al adaptador
                        allElements.addAll(adapterPosition + 1, subfamilies);
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return allElements.size();
    }
}