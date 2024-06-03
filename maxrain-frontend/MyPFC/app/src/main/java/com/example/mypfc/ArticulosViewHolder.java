package com.example.mypfc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import org.w3c.dom.Text;

public class ArticulosViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageArt;
    private TextView nombreData;
    private TextView precio;
    private TextView marcaMax;
    private ArticulosData artData;
    private Context context;


    public ArticulosViewHolder(@NonNull View itemView) {
        super(itemView);
        nombreData = (TextView) itemView.findViewById(R.id.textArticulo);
        imageArt = (ImageView) itemView.findViewById(R.id.image_subfamilia);
        precio = (TextView) itemView.findViewById(R.id.precioArticulo);
        context = itemView.getContext();


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetalleArticulo.class);
                intent.putExtra("codigo_articulo", artData.getCodigoArticulo());
                intent.putExtra("nombre", artData.getNombre());
                intent.putExtra("precio", String.valueOf(artData.getPrecio()));
                intent.putExtra("familia", artData.getCodigoFamilia());
                intent.putExtra("marca", artData.getCodigoMarca());
                intent.putExtra("imagen", artData.getImageURL());
                context.startActivity(intent);
            }
        });
    }

    public void bindArticulo(ArticulosData articulo) {
        artData = articulo;
        nombreData.setText(articulo.getNombre());
        precio.setText(String.valueOf(articulo.getPrecio())); // Convertimos el precio a String
        Glide.with(itemView.getContext())
                .load(articulo.getImageURL())
                .into(imageArt);
    }
}