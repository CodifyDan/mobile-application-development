package com.moschd002.studentportal;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class PortalViewHolder extends RecyclerView.ViewHolder {
    TextView textView;

    public PortalViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(android.R.id.text1);
    }
}
