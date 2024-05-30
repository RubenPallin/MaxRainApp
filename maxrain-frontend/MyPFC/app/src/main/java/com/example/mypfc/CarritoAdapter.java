package com.example.mypfc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoViewHolder> {

    private List<ArticulosData> articulosList;
    private Context context;
    public CarritoAdapter(List<ArticulosData> articulosList, Context context) {
        this.articulosList = articulosList;
        this.context = context;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_carrito_view_holder, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        ArticulosData articulo = articulosList.get(position);
        holder.bindCarrito(articulo);
    }

    @Override
    public int getItemCount() {
        return articulosList.size();
    }
}
