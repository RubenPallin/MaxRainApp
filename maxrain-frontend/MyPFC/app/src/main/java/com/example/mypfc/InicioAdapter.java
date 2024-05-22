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

public class InicioAdapter extends RecyclerView.Adapter<InicioViewHolder> {
    private int[] fotos;
    private Context context;
    public InicioAdapter(int[] fotos, Context context) {
        this.fotos = fotos;
        this.context = context;
    }
    @NonNull
    @Override
    public InicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_inicio_view_holder, parent, false);
        return new InicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InicioViewHolder holder, int position) {
        holder.imagenInicio.setImageResource(fotos[position]);
    }

    @Override
    public int getItemCount() {
        return fotos.length;
    }
}