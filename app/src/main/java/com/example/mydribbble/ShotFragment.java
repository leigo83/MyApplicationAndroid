package com.example.mydribbble;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mydribbble.BaseClass.HttpAsyncLoad;
import com.example.mydribbble.BaseFragment.SingleFragment;
import com.example.mydribbble.model.Shot;
import com.example.mydribbble.model.Shot2Detail;
import com.example.mydribbble.model.ShotDetail;
import com.example.mydribbble.model.User;
import com.example.mydribbble.utils.ImageUtils;
import com.example.mydribbble.utils.ModelUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class ShotFragment extends SingleFragment {
    public static final String ShotLink = "https://api.unsplash.com/photos/";
    public static final String UserLink = "https://api.unsplash.com/users/";
    public ShotDetail shotDetail;
    public String m_token;
    @Override
    protected Fragment createFragment() {
        return new ShotFragment();
    }

    public static Fragment createInstance() {
        return new ShotFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.shot_details, container, false);
        final Shot2Detail info = ModelUtils.toObject(getArguments().getString(ShotActivity.KEY_SHOT), new TypeToken<Shot2Detail>() {});
        m_token = info.token;
        HttpAsyncLoad<ShotDetail> getInfo = new HttpAsyncLoad<ShotDetail>(0) {
            @Override
            public String createQuery() {
                StringBuilder sb = new StringBuilder ();
                sb.append(ShotLink);
                sb.append(info.id);
                sb.append("?");
                sb.append("access_token=");
                sb.append(info.token);
                Log.d("ShotQuery", sb.toString());
                return sb.toString();
            }

            @Override
            protected void onPostExecute(Response response) {
                super.onPostExecute(response);
                try {
                    shotDetail = (ShotDetail) this.parseResponse(response, new TypeToken<ShotDetail>() {});
                    setViews(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                super.onPostExecute(response);
            }
        };
        getInfo.execute();

     /*   title_edit.setText(shot.title);
        descript_edit.setText(shot.description);
        StringBuilder sb = new StringBuilder ();
        for (int i = 0; i < shot.tags.size(); i++) {
            sb.append(shot.tags.get(i));
            sb.append(" ");
        }
        tag_edit.setText(sb.toString());
        Button saveButton = view.findViewById(R.id.save_change);
        Button deleteButton = view.findViewById(R.id.delete);
        title_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shot.title = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        descript_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shot.description = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tag_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String [] data = s.toString().split("\\s+");
                shot.tags.clear();
                for (int i = 0; i < data.length; i++) {
                    shot.tags.add(data[i]);
                    Log.d("LeiValue", data[i]);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = ModelUtils.toString(shot, new TypeToken<Shot>() {});
                updateData(body);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shot.images.clear();
                String body = ModelUtils.toString(shot, new TypeToken<Shot>() {});
                updateData(body);
            }
        });*/
        return view;
    }

    private void setViews(View view) {
        SimpleDraweeView image = view.findViewById(R.id.shot_image);
        final TextView likes = view.findViewById(R.id.likes);
        final TextView dislikes = view.findViewById(R.id.dislikes);
        TextView username = view.findViewById(R.id.username);
        TextView download = view.findViewById(R.id.shotdetail_download);
        TextView location = view.findViewById(R.id.location);
        TextView name = view.findViewById(R.id.name);
        TextView description = view.findViewById(R.id.description);
        TextView camera = view.findViewById(R.id.camera);
        TextView ownedPhotos = view.findViewById(R.id.ownedPhotos);

        Log.d("ShotQuery", String.valueOf(shotDetail.downloads));
        if (shotDetail.liked_by_user) {
            likes.setVisibility(View.VISIBLE);
            dislikes.setVisibility(View.GONE);
            likes.setText(String.valueOf(shotDetail.likes));
        } else {
            likes.setVisibility(View.GONE);
            dislikes.setVisibility(View.VISIBLE);
            dislikes.setText(String.valueOf(shotDetail.likes));
        }
        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likes.setVisibility(View.GONE);
                dislikes.setVisibility(View.VISIBLE);
                dislikes.setText(String.valueOf(shotDetail.likes));
                ShotListFragment.likeAction(2, null, shotDetail.id);
            }
        });
        dislikes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dislikes.setVisibility(View.GONE);
                likes.setVisibility(View.VISIBLE);
                likes.setText(String.valueOf(shotDetail.likes));
                RequestBody postBody = new FormBody.Builder().add("id", shotDetail.id).build();
                ShotListFragment.likeAction(1, postBody, shotDetail.id);
            }
        });
        download.setText(String.valueOf(shotDetail.downloads));
        SpannableString content = new SpannableString(shotDetail.user.username);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        username.setText(content);
        username.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new HttpAsyncLoad<User>(0) {
                    @Override
                    protected void onPostExecute(Response response) {
                        super.onPostExecute(response);
                        try {
                            User user = (User)this.parseResponse(response, new TypeToken<User>(){});
                            Intent intent = new Intent(getContext(), UserActivity.class);
                            intent.putExtra(UserActivity.USERINFO, ModelUtils.toString(user, new TypeToken<User>(){}));
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public String createQuery() {
                        StringBuilder sb = new StringBuilder ();
                        sb.append(UserLink);
                        sb.append(shotDetail.user.username);
                        sb.append("?");
                        sb.append("access_token=");
                        sb.append(m_token);
                        return sb.toString();
                    }
                }.execute();
            }
        });
        name.setText("Name: " + shotDetail.user.name);
        description.setText("Description: " + shotDetail.description);
        ownedPhotos.setText("OwnedPhotos: " + shotDetail.user.total_photos);
        if (shotDetail.location.city != null && shotDetail.location.country != null) {
            location.setText("Location: " + shotDetail.location.city + ", " + shotDetail.location.country);
        }
        if (shotDetail.exif.make != null && shotDetail.exif.model != null) {
            camera.setText("Camera Make/Model: " + shotDetail.exif.make + "/" + shotDetail.exif.model);
        }


        ImageUtils.loadShotImage(shotDetail.getImageUrl(), image);
    }

    private void updateData(String body) {
        Intent intent = new Intent(getActivity(), ShotListActivity.class);
        intent.putExtra(ShotListFragment.SHOT_DATA_KEY, body);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }
}
