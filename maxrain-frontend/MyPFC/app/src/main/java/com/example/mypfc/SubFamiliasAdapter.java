package com.example.mypfc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubFamiliasAdapter extends RecyclerView.Adapter<SubFamiliasHolder> {
    private List<MaxData> subfamilias;
    private LayoutInflater inflater;

    public SubFamiliasAdapter(List<MaxData> subfamilias, Context context) {
        this.subfamilias = subfamilias;
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public SubFamiliasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.activity_subfamilia_view_holder, parent, false);
        return new SubFamiliasHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubFamiliasHolder holder, int position) {
        MaxData subfamilia = subfamilias.get(position);
        holder.bind(subfamilia);
    }

    @Override
    public int getItemCount() {
        return subfamilias.size();
    }
}
