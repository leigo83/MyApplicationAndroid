package com.example.mydribbble;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    public static final String USER_FEATURE = "user_feature";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.user, container, false);
        final User user = ModelUtils.toObject(getArguments().getString(UserActivity.USERINFO), new TypeToken<User>() {});
        SimpleDraweeView image = view.findViewById(R.id.user_profile_image);
        final TextView username = view.findViewById(R.id.username);
        TextView name = view.findViewById(R.id.name);
        TextView total_likes = view.findViewById(R.id.total_likes);
        TextView total_photos = view.findViewById(R.id.total_photos);
        TextView total_collections = view.findViewById(R.id.total_collections);
        TextView followers_count = view.findViewById(R.id.followers_count);
        TextView following_count = view.findViewById(R.id.following_count);
        Button user_liked_photos = view.findViewById(R.id.user_liked_photos);
        Button user_collections = view.findViewById(R.id.user_collections);
        Button user_photos = view.findViewById(R.id.user_photos);
        ImageUtils.loadShotImage(user.getUserImageUrl(), image);
        username.setText(user.username);
        name.setText("Name: " + user.name);
        total_likes.setText("Total likes: " + user.total_likes);
        total_photos.setText("Total photos: " + user.total_photos);
        total_collections.setText("Total collections:" + user.total_collections);
        followers_count.setText("Followers count: " + user.followers_count);
        following_count.setText("Following count:" + user.following_count);

        SpannableString content = new SpannableString(user.name + " liked photos");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        user_liked_photos.setText(content);

        content = new SpannableString(user.name + " photos");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        user_photos.setText(content);

        content = new SpannableString(user.name + " collections");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        user_collections.setText(content);

        user_liked_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShotListActivity.class);
                intent.putExtra(USER_FEATURE, user.username + "/likes");
                startActivity(intent);
                getActivity().finish();
            }
        });
        user_collections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CollectionListActivity.class);
                intent.putExtra(USER_FEATURE, user.username + "/collections");
                startActivity(intent);
                getActivity().finish();
            }
        });
        user_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShotListActivity.class);
                intent.putExtra(USER_FEATURE, user.username + "/photos");
                startActivity(intent);
                getActivity().finish();
            }
        });
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
