package com.moschd002.studentportal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class PortalAdapter extends RecyclerView.Adapter<PortalViewHolder> {
    private List<PortalItem> mPortalItems;

    public PortalAdapter(List<PortalItem> mPortalItems) {
        this.mPortalItems = mPortalItems;
    }

    @NonNull
    @Override
    public PortalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(android.R.layout.simple_list_item_1, null);
        return new PortalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortalViewHolder portalViewHolder, int i) {
        PortalItem portalItem = mPortalItems.get(i);
        portalViewHolder.textView.setText(portalItem.getmPortalName());
    }

    @Override
    public int getItemCount() {
        return mPortalItems.size();
    }
}
