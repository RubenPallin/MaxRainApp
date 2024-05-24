package com.example.mypfc;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArticulosAdapter extends RecyclerView.Adapter<ArticulosViewHolder> {
    private List<ArticulosData> articulosList;
    private Context context;

    public ArticulosAdapter(List<ArticulosData> articulosList, Context context) {
        this.articulosList = articulosList;
        this.context = context;
    }
    @NonNull
    @Override
    public ArticulosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_articulos_view_holder, parent, false);
        // Retorna un nuevo objeto ProtagonistasViewHolder con la vista infladada
        return new ArticulosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticulosViewHolder holder, int position) {
        ArticulosData articulos = articulosList.get(position);
        holder.bindArticulo(articulos);
    }

    @Override
    public int getItemCount() {
        return articulosList.size();
    }
}