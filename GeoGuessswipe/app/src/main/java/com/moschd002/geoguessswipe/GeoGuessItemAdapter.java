package com.moschd002.geoguessswipe;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class GeoGuessItemAdapter extends RecyclerView.Adapter<GeoGuessItemViewHolder> {
    private List<GeoGuessItem> listGeoGuessItem;

    public GeoGuessItemAdapter(List<GeoGuessItem> listGeoGuessItem) {
        this.listGeoGuessItem = listGeoGuessItem;
    }

    @NonNull
    @Override
    public GeoGuessItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.geoguess_cell, viewGroup, false);
        return new GeoGuessItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeoGuessItemViewHolder geoGuessItemViewHolder, int i) {
        final GeoGuessItem geoGuessItem = listGeoGuessItem.get(i);
        geoGuessItemViewHolder.geoGuessItemImage.setImageResource(geoGuessItem.getmGeoGuessImageName());
    }

    @Override
    public int getItemCount() {
        return listGeoGuessItem.size();
    }
}
