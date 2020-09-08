package com.example.mydribbble;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mydribbble.BaseFragment.SingleFragment;
import com.example.mydribbble.model.User;
import com.example.mydribbble.utils.ImageUtils;
import com.example.mydribbble.utils.ModelUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;

public class UserFragment extends SingleFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.user, container, false);
        User user = ModelUtils.toObject(getArguments().getString(UserActivity.USERINFO), new TypeToken<User>() {});
        SimpleDraweeView image = view.findViewById(R.id.user_profile_image);
        TextView username = view.findViewById(R.id.username);
        TextView name = view.findViewById(R.id.name);
        TextView total_likes = view.findViewById(R.id.total_likes);
        TextView total_photos = view.findViewById(R.id.total_photos);
        TextView total_collections = view.findViewById(R.id.total_collections);
        TextView followers_count = view.findViewById(R.id.followers_count);
        TextView following_count = view.findViewById(R.id.following_count);
        ImageUtils.loadShotImage(user.getUserImageUrl(), image);
        username.setText(user.username);
        name.setText("Name: " + user.name);
        total_likes.setText("Total likes: " + user.total_likes);
        total_photos.setText("Total photos: " + user.total_photos);
        total_collections.setText("Total collections:" + user.total_collections);
        followers_count.setText("Followers count: " + user.followers_count);
        following_count.setText("Following count:" + user.following_count);
        return view;
    }

    @Override
    protected Fragment createFragment() {
        return new UserFragment();
    }

    public static Fragment createInstance() {
        return new UserFragment();
    }
}
