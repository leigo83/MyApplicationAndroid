package com.example.mydribbble;

import androidx.fragment.app.Fragment;

import com.example.mydribbble.BaseActivity.SingleFragmentActivity;

public class UploadImageActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return UploadImageFragment.createInstance();
    }
}
