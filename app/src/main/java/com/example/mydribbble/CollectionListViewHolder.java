package com.example.mydribbble;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

public class CollectionListViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public View cover;
    public SimpleDraweeView image;
    public SimpleDraweeView userImage;
    public TextView user;
    public ImageButton editButton;
    public CollectionListViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.collection_image);
        user = itemView.findViewById(R.id.collection_user);
        userImage = itemView.findViewById(R.id.collection_user_image);
        title = itemView.findViewById(R.id.collection_title);
        cover = itemView.findViewById(R.id.collection_clickable_cover);
        editButton = itemView.findViewById(R.id.collection_edit);
    }
}
