package com.example.mydribbble;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

public class UserListViewHolder extends RecyclerView.ViewHolder {
    public SimpleDraweeView userImage;
    public TextView username;
    public TextView likes;
    public TextView photos;
    public View cover;
    public UserListViewHolder(@NonNull View itemView) {
        super(itemView);
        userImage = itemView.findViewById(R.id.user_list_image);
        username = itemView.findViewById(R.id.username_list);
        likes = itemView.findViewById(R.id.user_total_likes);
        photos = itemView.findViewById(R.id.photos_count_list);
        cover = itemView.findViewById(R.id.user_clickable_cover);
    }
}
