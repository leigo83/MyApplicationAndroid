package com.example.mydribbble;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mydribbble.BaseFragment.SingleFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class LoginFragment extends SingleFragment {
    public static final int REQ_CODE = 100;
    public static final String AUTHORIZE_URL = "authorize_URL";
    public static final String CLIENT_ID_KEY = "client_id";
    public static final String CLIENT_SECRET_ID = "client_secret";
    public static final String REDIRECT_URI_KEY = "redirect_uri";
    public static final String SCOPE_KEY = "scope";
    public static final String CLIENT_ID = "Oes6KdBKq73rWSgS2HRkKPFdCWQTRSV_jRvIsa1GUJs";
    public static final String CLIENT_SECRET = "MM5NOPuvLaNSA5Fl_A_lz9LieVYMpxYak2vnEeM8-Uo";
    public static final String REDIRECT_URI = "https://www.google.com";
    public static final String SCOPE = "public+read_user+write_user+read_photos+write_photos+write_likes+write_followers+read_collections+write_collections";
    public static final String ACCESS_TOKEN = "access_token";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         View v = inflater.inflate(R.layout.login_fragment, container, false);
         TextView text = (TextView)v.findViewById(R.id.login_button);
         text.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getActivity(), AuthActivity.class);
                 intent.putExtra(AUTHORIZE_URL, getAuthorizeUrl());
                 startActivityForResult(intent, REQ_CODE);
             }
         });
         return v;
    }

    @Override
    protected Fragment createFragment() {
        LoginFragment curFragment = new LoginFragment();
        return curFragment;
    }

    public static Fragment createInstance() {
        LoginFragment curFragment = new LoginFragment();
        return curFragment;
    }

    public String getAuthorizeUrl() {
//        return Uri.parse("https://dribbble.com/oauth/authorize")
//                .buildUpon()
//                .appendQueryParameter(CLIENT_ID_KEY, CLIENT_ID)
//                .appendQueryParameter(REDIRECT_URI_KEY, REDIRECT_URI)
//                .appendQueryParameter(SCOPE_KEY, SCOPE)
//                .build().toString();
        return Uri.parse("https://unsplash.com/oauth/authorize?client_id=Oes6KdBKq73rWSgS2HRkKPFdCWQTRSV_jRvIsa1GUJs" +
                         "&redirect_uri=https://www.google.com&response_type=code" +
                         "&scope=public+read_user+write_user+read_photos+write_photos+write_likes+write_followers+read_collections+write_collections").buildUpon().build().toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQ_CODE) {
            final String authorCode = data.getStringExtra(AuthFragment.KEY_CODE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String token = getTokens(authorCode);
                        Intent intent = new Intent (getActivity(), ShotListActivity.class);
                        intent.putExtra(ACCESS_TOKEN, token);
                        startActivity(intent);
                        getActivity().finish();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private String getTokens(String authorCode) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        Log.d("authorCode", authorCode);
        RequestBody postbody = new FormBody.Builder()
                .add(CLIENT_ID_KEY, CLIENT_ID)
                .add(CLIENT_SECRET_ID, CLIENT_SECRET)
                .add(AuthFragment.KEY_CODE, authorCode)
                .add(REDIRECT_URI_KEY, REDIRECT_URI)
                .add("grant_type", "authorization_code")
                .build();
        Request request = new Request.Builder()
                .url("https://unsplash.com/oauth/token")
                .post(postbody)
                .build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        JSONObject obj = new JSONObject(responseString);
        String token = obj.getString(ACCESS_TOKEN);
        Log.d("TOKEN", token);
        return token;
    }
}
