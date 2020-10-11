package com.example.mydribbble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mydribbble.BaseClass.HttpAsyncLoad;
import com.example.mydribbble.BaseFragment.SingleFragment;
import com.example.mydribbble.model.Collection;
import com.example.mydribbble.model.User;
import com.example.mydribbble.utils.ImageUtils;
import com.example.mydribbble.utils.ModelUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        TextView firstName = view.findViewById(R.id.first_name);
        TextView lastName = view.findViewById(R.id.last_name);
        TextView email = view.findViewById(R.id.email);
        TextView url = view.findViewById(R.id.url);
        TextView location = view.findViewById(R.id.location);
        TextView bio = view.findViewById(R.id.bio);
        TextView instagram = view.findViewById(R.id.instagram_username);
        TextView total_likes = view.findViewById(R.id.total_likes);
        TextView total_photos = view.findViewById(R.id.total_photos);
        TextView total_collections = view.findViewById(R.id.total_collections);
        TextView followers_count = view.findViewById(R.id.followers_count);
        TextView following_count = view.findViewById(R.id.following_count);
        Button user_liked_photos = view.findViewById(R.id.user_liked_photos);
        Button user_collections = view.findViewById(R.id.user_collections);
        Button user_photos = view.findViewById(R.id.user_photos);
        ImageUtils.loadShotImage(user.getUserImageUrl(), image);
        final EditText firstName_edit = view.findViewById(R.id.first_name_edit);
        final EditText lastName_edit = view.findViewById(R.id.last_name_edit);
        final EditText email_edit = view.findViewById(R.id.email_edit);
        final EditText url_edit = view.findViewById(R.id.url_edit);
        final EditText location_edit = view.findViewById(R.id.location_edit);
        final EditText bio_edit = view.findViewById(R.id.bio_edit);
        final EditText instagram_edit = view.findViewById(R.id.instagram_username_edit);
        Button save = view.findViewById(R.id.save_user_info);
        if (!user.username.equals(Unsplash.username)) {
            username.setText(user.username);
            firstName.setText("First Name: " + user.first_name);
            lastName.setText("Last Name: " + user.last_name);
            email.setText("Emmail: " + user.email);
            url.setText("Url: " + user.portfolio_url);
            location.setText("Location: " + user.location);
            bio.setText("Bio: "+ user.bio);
            instagram.setText("Instagram: " + user.instgram_username);
        } else {
            username.setText(user.username);
            firstName.setText("First Name: ");
            lastName.setText("Last Name: ");
            email.setText("Email: ");
            url.setText("Url: ");
            location.setText("Location: ");
            bio.setText("Bio: ");
            instagram.setText("Instagram: ");
            firstName_edit.setVisibility(View.VISIBLE);
            lastName_edit.setVisibility(View.VISIBLE);
            email_edit.setVisibility(View.VISIBLE);
            url_edit.setVisibility(View.VISIBLE);
            location_edit.setVisibility(View.VISIBLE);
            bio_edit.setVisibility(View.VISIBLE);
            instagram_edit.setVisibility(View.VISIBLE);
            firstName_edit.setText(user.first_name);
            lastName_edit.setText(user.last_name);
            email_edit.setText(user.email);
            url_edit.setText(user.portfolio_url);
            location_edit.setText(user.location);
            bio_edit.setText(user.bio);
            instagram_edit.setText(user.instgram_username);
            save.setVisibility(View.VISIBLE);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final RequestBody postBody = new FormBody.Builder()
                            .add("access_token", Unsplash.token)
                            .add("username", user.username)
                            .add("first_name", firstName_edit.getText().toString())
                            .add("last_name", lastName_edit.getText().toString())
                            .add("email", email_edit.getText().toString())
                            .add("url", url_edit.getText().toString())
                            .add("location", location_edit.getText().toString())
                            .add("bio", bio_edit.getText().toString())
                            .add("instagram_username", instagram_edit.getText().toString()).build();
                    new HttpAsyncLoad<User>(3, postBody) {

                        @Override
                        protected void onPostExecute(Response response) {
                            FragmentManager fm = getFragmentManager();
                            if (fm.getBackStackEntryCount() > 0) {
                                Log.i("ShotQuery", "" +
                                        "popping backstack");
                                fm.popBackStack();
                            } else {
//                                Intent resultIntent = new Intent(getActivity(), CollectionListActivity.class);
//                                resultIntent.putExtra("ID", cur_id + "," + "1");
//                                getActivity().setResult(Activity.RESULT_OK,  resultIntent);
                                getActivity().finish();
                            }
                        }

                        @Override
                        public String createQuery() {
                            StringBuilder sb = new StringBuilder();
                            sb.append("https://api.unsplash.com/me");
                            return sb.toString();
                        }
                    }.execute();
                }
            });
        }
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
