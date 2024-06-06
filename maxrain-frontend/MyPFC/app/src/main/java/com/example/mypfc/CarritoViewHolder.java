package com.example.mypfc;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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
    private ImageButton papelera;

    public CarritoViewHolder(@NonNull View itemView) {
        super(itemView);
        nombreArticulo = (TextView) itemView.findViewById(R.id.text_carrito);
        precioArticulo = (TextView) itemView.findViewById(R.id.precio_carrito);
        imagenArticulo = (ImageView) itemView.findViewById(R.id.image_carrito);
        papelera = (ImageButton) itemView.findViewById(R.id.papelera);
    }

    public void bindCarrito(ArticulosData articulo, final int position, final CarritoAdapter adapter) {
        nombreArticulo.setText(articulo.getNombre());
        precioArticulo.setText(String.valueOf(articulo.getPrecio()));
        // Cargar la imagen utilizando Glide
        Glide.with(itemView.getContext())
                .load(articulo.getImageURL())
                .into(imagenArticulo);
        // Configurar el OnClickListener para el botón de la papelera
        papelera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.eliminarArticuloDelCarrito(articulo.getCodigoArticulo(), position);
            }
        });
    }
}