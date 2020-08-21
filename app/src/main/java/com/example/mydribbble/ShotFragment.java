package com.example.mydribbble;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mydribbble.BaseClass.HttpAsyncLoad;
import com.example.mydribbble.BaseFragment.SingleFragment;
import com.example.mydribbble.model.Shot;
import com.example.mydribbble.model.Shot2Detail;
import com.example.mydribbble.model.ShotDetail;
import com.example.mydribbble.utils.ImageUtils;
import com.example.mydribbble.utils.ModelUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class ShotFragment extends SingleFragment {
    public static final String ShotLink = "https://api.unsplash.com/photos/";
    public ShotDetail shotDetail;
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
        HttpAsyncLoad<ShotDetail> getInfo = new HttpAsyncLoad<ShotDetail>() {
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
        EditText title_edit = view.findViewById(R.id.title_edit);
        EditText descript_edit = view.findViewById(R.id.descript_edit);
        EditText tag_edit = view.findViewById(R.id.tag_edit);
        Log.d("ShotQuery", shotDetail.getImageUrl());

     //   title_edit.setText(shotDetail.alt_description);
        title_edit.setText(shotDetail.description);
        title_edit.setText(shotDetail.links.html);
        descript_edit.setText(Boolean.toString(shotDetail.liked_by_user));
        tag_edit.setText(Integer.toString(shotDetail.downloads));
        ImageUtils.loadShotImage(shotDetail.getImageUrl(), image);
    }

    private void updateData(String body) {
        Intent intent = new Intent(getActivity(), ShotListActivity.class);
        intent.putExtra(ShotListFragment.SHOT_DATA_KEY, body);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }
}
