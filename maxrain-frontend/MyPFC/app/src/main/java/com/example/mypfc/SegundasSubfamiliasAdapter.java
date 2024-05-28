package com.example.mypfc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SegundasSubfamiliasAdapter extends RecyclerView.Adapter<SegundasSubfamiliasHolder>{
    private List<MaxData> segundasSubfamilias;
    private LayoutInflater inflater;

    public SegundasSubfamiliasAdapter(List<MaxData> segundasSubfamilias, Context context) {
        this.segundasSubfamilias = segundasSubfamilias;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SegundasSubfamiliasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.activity_segundas_subfamilias_holder, parent, false);
        return new SegundasSubfamiliasHolder(itemView, segundasSubfamilias);
    }

    @Override
    public void onBindViewHolder(@NonNull SegundasSubfamiliasHolder holder, int position) {
        MaxData subfamilia = segundasSubfamilias.get(position);
        holder.bindSegundo(subfamilia);
    }

    @Override
    public int getItemCount() {
        return segundasSubfamilias.size();
    }
}
