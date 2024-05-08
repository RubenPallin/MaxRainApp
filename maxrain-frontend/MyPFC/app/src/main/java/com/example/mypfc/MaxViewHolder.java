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

public class MaxViewHolder extends RecyclerView.ViewHolder {

    private MaxData maxData;
    private TextView maxName;
    private ImageView maxImage;

    public MaxViewHolder(@NonNull View itemView) {
        super(itemView);
        maxName = (TextView) itemView.findViewById(R.id.text_view_holder);
        maxImage = (ImageView) itemView.findViewById(R.id.image_view_holder);
    }

    public void bindMaxMethod(MaxData maxData, Activity activity) {
        maxName.setText(maxData.getName());

        // Cargar la imagen con Glide
        Glide.with(itemView)
                .load(maxData.getImageURL()) // Aquí debes proporcionar la URL de la imagen
                .into(maxImage); // Reemplaza "imageView" con el ImageView donde deseas mostrar la imagen
    }

}