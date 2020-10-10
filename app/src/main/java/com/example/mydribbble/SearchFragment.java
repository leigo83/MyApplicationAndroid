package com.example.mydribbble;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.mydribbble.BaseClass.HttpAsyncLoad;
import com.example.mydribbble.BaseFragment.SingleFragment;
import com.example.mydribbble.model.Collection;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

public class SearchFragment extends SingleFragment {
    public static final String link = "https://api.unsplash.com/search/";
    public static final String QUERYINFO = "queryInfo";
    public String queryContent;
    @Override
    protected Fragment createFragment() {
        return new SearchFragment();
    }

    public static Fragment createInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        TextView textView = view.findViewById(R.id.search_title);
        final EditText searchContent = view.findViewById(R.id.search_content);
        Button submit = view.findViewById(R.id.search_submit);
        final String token = getArguments().getString("Token");
        final String type = getArguments().getString("Type");
        textView.setText("search " + type + ":");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryContent = searchContent.getText().toString();
                StringBuilder sb = new StringBuilder();
                sb.append(link);
                sb.append(type);
                sb.append("?");
                sb.append("access_token=" + token);
                sb.append("&query="+queryContent);
                if (type.equals("photos")) {
                    Intent intent = new Intent(getActivity(), ShotListActivity.class);
                    intent.putExtra(QUERYINFO, sb.toString());
                    startActivity(intent);
                } else if (type.equals("collections")) {
                    Intent intent = new Intent(getActivity(), CollectionListActivity.class);
                    intent.putExtra(QUERYINFO, sb.toString());
                    startActivity(intent);
                } else if (type.equals("users")) {
                    Intent intent = new Intent(getActivity(), UserListActivity.class);
                    intent.putExtra(QUERYINFO, sb.toString());
                    startActivity(intent);
                }
            }
        });
        return view;
    }
}
