package com.example.mypfc;

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

import java.util.List;

public class SegundasSubfamiliasHolder extends RecyclerView.ViewHolder {

    private TextView subfamiliaName;
    private MaxData subfamiliaData;
    private ImageView subfamiliaImage;
    private Context context;
    private List<MaxData> segundasSubfamiliasList;

    public SegundasSubfamiliasHolder(@NonNull View itemView, List<MaxData> segundasSubfamiliasList) {
        super(itemView);
        this.segundasSubfamiliasList = segundasSubfamiliasList;
        subfamiliaName = itemView.findViewById(R.id.text_view_segunda_subfamilia);
        subfamiliaImage = itemView.findViewById(R.id.image_segunda_subfamilia);
        context = itemView.getContext();

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    MaxData clickedSegundaSubfamilia = segundasSubfamiliasList.get(position);
                    Intent intent = new Intent(context, Articulos.class);
                    // Aquí debes pasar el código de la familia como un Integer
                    intent.putExtra("codigo_familia", String.format("%06d", clickedSegundaSubfamilia.getCodigoFamilia()));
                    context.startActivity(intent);
                }
            }
        });
    }

    public void bindSegundo(MaxData subfamilia) {
        this.subfamiliaData = subfamilia;
        subfamiliaName.setText(subfamilia.getNombre());

        // Cargar la imagen con Glide
        Glide.with(itemView)
                .load(subfamiliaData.getImageURL())
                .into(subfamiliaImage);
    }
}