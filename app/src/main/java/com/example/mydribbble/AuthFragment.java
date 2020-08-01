package com.example.mydribbble;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class AuthFragment extends Fragment {
    public static final String AUTH_URL_KEY = "auth_url_key";
    public static final String KEY_CODE = "code";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.webview_fragment, container, false);
        WebView webview = v.findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //        Log.d("authorCode", url);
                if (url.startsWith(LoginFragment.REDIRECT_URI)) {
                    Uri uri = Uri.parse(url);
                    String code = uri.getQueryParameter(KEY_CODE);
                    Intent resultIntent = new Intent(getActivity(), LoginActivity.class);
                    resultIntent.putExtra(KEY_CODE, code);
                    Log.d("authorCode", code);
                    getActivity().setResult(RESULT_OK, resultIntent);
                    getActivity().finish();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        String url = getArguments().getString(AUTH_URL_KEY);
        Log.d("authorCode", url);
        webview.loadUrl(url);
        return v;
    }


    public static Fragment createInstance() {
        return new AuthFragment();
    }
}
