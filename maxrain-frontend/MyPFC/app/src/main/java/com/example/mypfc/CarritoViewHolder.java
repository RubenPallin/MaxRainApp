package com.example.mypfc;

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

public class CarritoViewHolder extends RecyclerView.ViewHolder {

    private TextView nombreArticulo;
    private TextView precioArticulo;
    private ImageView imagenArticulo;

    public CarritoViewHolder(@NonNull View itemView) {
        super(itemView);
        nombreArticulo = (TextView) itemView.findViewById(R.id.text_carrito);
        precioArticulo = (TextView) itemView.findViewById(R.id.precio_carrito);
        imagenArticulo = (ImageView) itemView.findViewById(R.id.image_carrito);
    }

    public void bindCarrito(ArticulosData articulo) {
        nombreArticulo.setText(articulo.getNombre());
        precioArticulo.setText(String.valueOf(articulo.getPrecio()));
        // Cargar la imagen utilizando Glide
        Glide.with(itemView.getContext())
                .load(articulo.getImageURL())
                .into(imagenArticulo);
    }
}