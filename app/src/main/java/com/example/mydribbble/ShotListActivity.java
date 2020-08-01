package com.example.mydribbble;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.mydribbble.BaseActivity.SingleFragmentActivity;

public class ShotListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        Bundle bundle = new Bundle();
        Intent intent = getIntent();
        String token = intent.getStringExtra(LoginFragment.ACCESS_TOKEN);
        bundle.putString("Token", token);
        Fragment fragment = ShotListFragment.createInstance();
        fragment.setArguments(bundle);
        return fragment;
    }
}
