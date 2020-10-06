package com.example.mydribbble;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.mydribbble.BaseActivity.SingleFragmentActivity;
import com.example.mydribbble.CreateCollectionFragment;

public class CollectionEditActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        Fragment fragment = new CreateCollectionFragment();
        Bundle bundle = new Bundle ();
        Intent intent = getIntent();
        String info = intent.getStringExtra(CollectionListFragment.COLLECTIONINFO);
        bundle.putString("Token", Unsplash.token);
        bundle.putString("INFO", info);
        fragment.setArguments(bundle);
        return fragment;
    }
}
