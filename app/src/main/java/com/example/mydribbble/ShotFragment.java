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

import com.example.mydribbble.BaseFragment.SingleFragment;
import com.example.mydribbble.model.Shot;
import com.example.mydribbble.utils.ImageUtils;
import com.example.mydribbble.utils.ModelUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import static android.app.Activity.RESULT_OK;

public class ShotFragment extends SingleFragment {
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
        View view = inflater.inflate(R.layout.shot_details, container, false);
        String shot_str = getArguments().getString(ShotActivity.KEY_SHOT);
        final Shot shot = ModelUtils.toObject(shot_str, new TypeToken<Shot>(){});
        SimpleDraweeView image = view.findViewById(R.id.shot_image);
        EditText title_edit = view.findViewById(R.id.title_edit);
        EditText descript_edit = view.findViewById(R.id.descript_edit);
        EditText tag_edit = view.findViewById(R.id.tag_edit);
        ImageUtils.loadShotImage(shot, image);
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

    private void updateData(String body) {
        Intent intent = new Intent(getActivity(), ShotListActivity.class);
        intent.putExtra(ShotListFragment.SHOT_DATA_KEY, body);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }
}
