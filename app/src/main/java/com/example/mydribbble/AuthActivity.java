package com.example.mydribbble;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mydribbble.BaseActivity.SingleFragmentActivity;

public class AuthActivity extends SingleFragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected Fragment createFragment() {
        Bundle bundle = new Bundle();
        Intent intent = getIntent();
        String url = intent.getStringExtra(LoginFragment.AUTHORIZE_URL);
        bundle.putString(AuthFragment.AUTH_URL_KEY, url);
        Fragment fragment = AuthFragment.createInstance(); //LoginFragment.createInstance();
        fragment.setArguments(bundle);
        return fragment;
    }
}

//public class AuthActivity extends SingleFragmentActivity {//AppCompatActivity {
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//  //      setContentView(R.layout.fragment_container_activity);
//    }
//
//    @Override
//    protected Fragment createFragment() {
//        return LoginFragment.createInstance();
//    }
//}
