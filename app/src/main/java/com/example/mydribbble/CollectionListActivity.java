package com.example.mydribbble;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.mydribbble.BaseActivity.SingleFragmentActivity;

public class CollectionListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        Fragment fragment = new CollectionListFragment();
        Bundle bundle = new Bundle();
        Intent intent = getIntent();
        bundle.putString("Token", Unsplash.token);
        bundle.putString("Userfeature", intent.getStringExtra(UserFragment.USER_FEATURE));
        fragment.setArguments(bundle);
        return fragment;
    }
}
