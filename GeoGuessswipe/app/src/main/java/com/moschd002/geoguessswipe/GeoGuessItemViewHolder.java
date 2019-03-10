package com.moschd002.geoguessswipe;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public class GeoGuessItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView geoGuessItemImage;
    public View view;

    public GeoGuessItemViewHolder(View itemView) {
        super(itemView);
        geoGuessItemImage = itemView.findViewById(R.id.geoGuessImageView);
        view = itemView;
    }

}
