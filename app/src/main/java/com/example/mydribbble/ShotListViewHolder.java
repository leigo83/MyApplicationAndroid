package com.example.mydribbble;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

public class ShotListViewHolder extends RecyclerView.ViewHolder {
    public View cover;
    public TextView shot_bucket_count;
    public TextView shot_like_count;
    public TextView shot_view_count;
    public SimpleDraweeView image;
    public ShotListViewHolder(@NonNull View itemView) {
        super(itemView);
        shot_bucket_count = itemView.findViewById(R.id.shot_bucket_count);
        shot_like_count = itemView.findViewById(R.id.shot_like_count);
        shot_view_count = itemView.findViewById(R.id.shot_view_count);
        image = itemView.findViewById(R.id.shot_image);
        cover = itemView.findViewById(R.id.shot_clickable_cover);
    }
}
