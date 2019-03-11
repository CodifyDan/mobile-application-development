package com.moschd002.bucketlist;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;


public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.BucketListItemViewHolder> {
    private List<BucketListItem> mBucketListItems;
    private ButtonClickListener buttonClickListener;

    public interface ButtonClickListener {
        void onSelect(BucketListItem bucketListItem);
    }

    public BucketListAdapter(List<BucketListItem> mBucketListItems, ButtonClickListener buttonClickListener) {
        this.mBucketListItems = mBucketListItems;
        this.buttonClickListener = buttonClickListener;
    }

    @NonNull
    @Override
    public BucketListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.simple_checkbox_list_item, null);
        return new BucketListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BucketListItemViewHolder bucketListItemViewHolder, int i) {
        BucketListItem bucketListItem = mBucketListItems.get(i);
        bucketListItemViewHolder.mCheckBox.setChecked(bucketListItem.isChecked());
        bucketListItemViewHolder.mTitle.setText(bucketListItem.getTitle());
        bucketListItemViewHolder.mDescription.setText(bucketListItem.getDescription());

        if (bucketListItem.isChecked()) {
            bucketListItemViewHolder.mTitle.setPaintFlags(bucketListItemViewHolder.mCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            bucketListItemViewHolder.mDescription.setPaintFlags(bucketListItemViewHolder.mCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            bucketListItemViewHolder.mTitle.setPaintFlags(bucketListItemViewHolder.mCheckBox.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            bucketListItemViewHolder.mDescription.setPaintFlags(bucketListItemViewHolder.mCheckBox.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return mBucketListItems.size();
    }

    public class BucketListItemViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;
        TextView mTitle;
        TextView mDescription;

        public BucketListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            View view = itemView.findViewById(R.id.detail);

            mCheckBox = itemView.findViewById(R.id.cb);
            mTitle = view.findViewById(R.id.title);
            mDescription = view.findViewById(R.id.description);

            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClickListener.onSelect(mBucketListItems.get(getAdapterPosition()));
                }
            });

        }
    }
}
