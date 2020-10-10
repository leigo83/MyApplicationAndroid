package com.example.mydribbble;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.mydribbble.BaseActivity.SingleFragmentActivity;
import com.example.mydribbble.model.User;

public class UserListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        Fragment fragment = new UserListFragment();
        Bundle bundle = new Bundle();
        Intent intent = getIntent();
        bundle.putString("Token", Unsplash.token);
        bundle.putString(SearchFragment.QUERYINFO, intent.getStringExtra(SearchFragment.QUERYINFO));
        fragment.setArguments(bundle);
        return fragment;
    }
}
