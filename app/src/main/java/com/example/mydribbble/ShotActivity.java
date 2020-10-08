package com.example.mydribbble;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.mydribbble.BaseActivity.SingleFragmentActivity;

public class ShotActivity extends SingleFragmentActivity {
    public static final String KEY_SHOT = "shot";
    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SHOT, intent.getStringExtra(KEY_SHOT));
        bundle.putString(CollectionListFragment.COLLECTIONUSERNAME, intent.getStringExtra(CollectionListFragment.COLLECTIONUSERNAME));
        bundle.putString(CollectionListFragment.COLLECTIONID, intent.getStringExtra(CollectionListFragment.COLLECTIONID));
        Fragment shotFragment = ShotFragment.createInstance();
        shotFragment.setArguments(bundle);
        return shotFragment;
    }
}
