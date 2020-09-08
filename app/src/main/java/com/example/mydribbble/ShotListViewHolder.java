package com.example.mydribbble;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

public class ShotListViewHolder extends RecyclerView.ViewHolder {
    public View cover;
    public TextView shot_view_count;
    public TextView shot_like_count;
    public TextView shot_user;
    public TextView shot_download_count;
    public ImageButton shot_like_action;
    public ImageButton shot_dislike_action;
    public SimpleDraweeView user_image;
    public SimpleDraweeView image;
    public ShotListViewHolder(@NonNull View itemView) {
        super(itemView);
        shot_like_count = itemView.findViewById(R.id.shot_like_count);
        shot_view_count = itemView.findViewById(R.id.shot_view_count);
        shot_download_count = itemView.findViewById(R.id.shot_download_count);
        shot_user = itemView.findViewById(R.id.shot_user);
        shot_dislike_action = itemView.findViewById(R.id.shot_dislike_action);
        shot_like_action = itemView.findViewById(R.id.shot_like_action);
        user_image = itemView.findViewById(R.id.user_image);
        image = itemView.findViewById(R.id.shot_image);
        cover = itemView.findViewById(R.id.shot_clickable_cover);
    }
}
