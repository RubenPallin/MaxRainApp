package com.example.mypfc;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class SubFamiliasHolder extends RecyclerView.ViewHolder {
    private TextView subfamiliaName;
    private MaxData subfamiliaData;
    private ImageView subfamiliaImage;
    public SubFamiliasHolder(@NonNull View itemView) {
        super(itemView);
        subfamiliaName = (TextView) itemView.findViewById(R.id.text_view_subfamilia);
        subfamiliaImage = (ImageView) itemView.findViewById(R.id.image_subfamilia);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aquí puedes manejar el clic en una subfamilia si es necesario
            }
        });
    }

    public void bind(MaxData subfamilia) {
        this.subfamiliaData = subfamilia;
        subfamiliaName.setText(subfamilia.getName());

        // Cargar la imagen con Glide
        Glide.with(itemView)
                .load(subfamiliaData.getImageURL())
                .into(subfamiliaImage);
    }
}
