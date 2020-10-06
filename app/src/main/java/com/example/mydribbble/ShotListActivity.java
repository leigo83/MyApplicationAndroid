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
        String token = Unsplash.token;//intent.getStringExtra(LoginFragment.ACCESS_TOKEN);
        String collectionId = intent.getStringExtra(CollectionListFragment.COLLECTIONID);
        String userfeature = intent.getStringExtra(UserFragment.USER_FEATURE);
        bundle.putString("Token", token);
        bundle.putString("CollectionId", collectionId);
        bundle.putString("Userfeature", userfeature);
        Fragment fragment = ShotListFragment.createInstance();
        fragment.setArguments(bundle);
        return fragment;
    }
}
