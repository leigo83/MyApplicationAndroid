package com.example.mydribbble;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.mydribbble.BaseActivity.SingleFragmentActivity;

public class UserActivity extends SingleFragmentActivity {
    public static final String USERINFO = "userInfo";

    @Override
    protected Fragment createFragment() {
        Bundle bundle = new Bundle();
        Intent intent = getIntent();
        Fragment fragment = UserFragment.createInstance();
        bundle.putString(USERINFO, intent.getStringExtra(USERINFO));
        fragment.setArguments(bundle);
        return fragment;
    }
}
