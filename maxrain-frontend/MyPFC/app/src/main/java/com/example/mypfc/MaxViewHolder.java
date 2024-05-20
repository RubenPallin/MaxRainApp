package com.example.mypfc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maxData != null) { // Verificar si maxData no es nulo
                    Context context = v.getContext();
                    Intent intent = new Intent(context, SubFamilias.class);
                    intent.putExtra("codigo_familia", maxData.getCodigoFamilia());
                    context.startActivity(intent);
                }
            }
        });

    }

    public void bindMaxMethod(MaxData maxData, Activity activity) {
        this.maxData = maxData;
        maxName.setText(maxData.getName());

        Log.d("Glide", "URL de la imagen: " + maxData.getImageURL());

        // Cargar la imagen con Glide
        Glide.with(itemView)
                .load(maxData.getImageURL())
                .into(maxImage);


    }

}