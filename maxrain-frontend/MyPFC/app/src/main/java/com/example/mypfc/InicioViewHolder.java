package com.example.mypfc;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

public class InicioViewHolder  extends RecyclerView.ViewHolder {
    ImageView imagenInicio;

    public InicioViewHolder(@NonNull View itemView) {
        super(itemView);
        imagenInicio = (ImageView) itemView.findViewById(R.id.imagen_inicio);
    }

}