package com.moschd002.gamebacklog.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moschd002.gamebacklog.db.model.GameBacklogItem;
import com.moschd002.gamebacklog.R;

import java.sql.Date;
import java.text.DateFormat;
import java.util.List;


public class GameBacklogAdapter extends RecyclerView.Adapter<GameBacklogAdapter.GameBacklogItemViewHolder> {
    private List<GameBacklogItem> mGameBacklogItems;
    private ButtonClickListener buttonClickListener;

    public interface ButtonClickListener {
        void onSelect(GameBacklogItem gameBacklogItem);
    }

    public GameBacklogAdapter(List<GameBacklogItem> mGameBacklogItems, ButtonClickListener buttonClickListener) {
        this.mGameBacklogItems = mGameBacklogItems;
        this.buttonClickListener = buttonClickListener;
    }

    @NonNull
    @Override
    public GameBacklogItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.game_backlog_cardview_list_item, viewGroup, false);
        return new GameBacklogItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameBacklogItemViewHolder gameBacklogItemViewHolder, int i) {
        GameBacklogItem gameBacklogItem = mGameBacklogItems.get(i);
        gameBacklogItemViewHolder.mTitle.setText(gameBacklogItem.getTitle());
        gameBacklogItemViewHolder.mPlatform.setText(gameBacklogItem.getPlatform());
        gameBacklogItemViewHolder.mStatus.setText(gameBacklogItem.getStatus());


        Date date = new Date(gameBacklogItem.getDate().getTime());
        String createdOn = DateFormat.getDateInstance().format(date);

        gameBacklogItemViewHolder.mDate.setText(createdOn);
    }

    @Override
    public int getItemCount() {
        return mGameBacklogItems.size();
    }

    public class GameBacklogItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mPlatform;
        TextView mStatus;
        TextView mDate;

        public GameBacklogItemViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.titleText);
            mStatus = itemView.findViewById(R.id.statusText);
            mPlatform = itemView.findViewById(R.id.platformText);
            mPlatform.setSingleLine();
            mPlatform.setEllipsize(TextUtils.TruncateAt.END);
            mDate = itemView.findViewById(R.id.dateText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClickListener.onSelect(mGameBacklogItems.get(getAdapterPosition()));
                }
            });

        }
    }
}
