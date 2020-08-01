package com.example.mydribbble.BaseActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.mydribbble.R;

public abstract class SingleFragmentActivity extends  FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_activity);
        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            Fragment fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container_id, fragment).commit();
        }
    }

    protected abstract Fragment createFragment();
}
