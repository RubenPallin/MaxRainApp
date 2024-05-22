package com.example.mypfc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ArticulosViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageArt;
    private TextView nombreData;
    private TextView precio;
    private ArticulosData artData;


    public ArticulosViewHolder(@NonNull View itemView) {
        super(itemView);
        nombreData = (TextView) itemView.findViewById(R.id.textArticulo);
        imageArt = (ImageView) itemView.findViewById(R.id.imageArticulo);
        precio = (TextView) itemView.findViewById(R.id.precioArticulo);
    }

    public void bindArticulos(ArticulosData artData) {
        this.artData = artData;
        this.nombreData.setText(artData.getNombre());

        // Cargar la imagen con Glide
        Glide.with(itemView)
                .load(artData.getImageURL())
                .into(imageArt);
        this.precio.setText((CharSequence) precio);
    }
}