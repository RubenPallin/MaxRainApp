package com.example.mypfc;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SubFamiliasHolder extends RecyclerView.ViewHolder {
    private TextView subfamiliaName;
    private MaxData subfamiliaData;
    private ImageView subfamiliaImage;
    private Context context;
    private List<MaxData> subfamiliasList;
    public SubFamiliasHolder(@NonNull View itemView, List<MaxData> subfamiliasList) {
        super(itemView);
        this.subfamiliasList = subfamiliasList;
        subfamiliaName = (TextView) itemView.findViewById(R.id.text_view_subfamilia);
        subfamiliaImage = (ImageView) itemView.findViewById(R.id.image_subfamilia);
        context = itemView.getContext();

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    MaxData clickedSubfamilia = subfamiliasList.get(position);
                    Log.d("SubFamilias", "Código de subfamilia: " + String.format("%04d", clickedSubfamilia.getCodigoFamilia()));
                    Intent intent = new Intent(context, SegundasSubFamilias.class);
                    intent.putExtra("codigo_subfamilia", String.format("%04d", clickedSubfamilia.getCodigoFamilia()));
                    context.startActivity(intent);
                }
            }
        });
    }

    public void bind(MaxData subfamilia) {
        this.subfamiliaData = subfamilia;
        subfamiliaName.setText(subfamilia.getNombre());

        // Cargar la imagen con Glide
        Glide.with(itemView)
                .load(subfamiliaData.getImageURL())
                .into(subfamiliaImage);
    }
}
