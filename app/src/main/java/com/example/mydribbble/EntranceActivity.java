package com.example.mydribbble;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.mydribbble.BaseActivity.SingleFragmentActivity;
import com.example.mydribbble.model.User;
import com.example.mydribbble.utils.ImageUtils;
import com.example.mydribbble.utils.ModelUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.widget.Toolbar;


public class EntranceActivity extends AppCompatActivity {
    private String m_token;
    private String m_user;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private ActionBarDrawerToggle drawerToggle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();
        m_token = intent.getStringExtra(LoginFragment.ACCESS_TOKEN);
    //    m_user = ModelUtils.toObject(intent.getStringExtra(UserActivity.USERINFO), new TypeToken<User>(){});
        m_user = intent.getStringExtra(UserActivity.USERINFO);
        setTitle("Unsplash");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        setup_drawer();

        if (savedInstanceState == null) {
            Fragment fragment = ShotListFragment.createInstance();
            Bundle bundle = new Bundle();
            bundle.putString("Token", m_token);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

        private void setup_drawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
        drawerLayout.addDrawerListener(drawerToggle);

        View headerView = navigationView.getHeaderView(0);
        User user = ModelUtils.toObject(m_user, new TypeToken<User>(){});
        SimpleDraweeView image = headerView.findViewById(R.id.nav_header_user_picture);
        ImageUtils.loadShotImage(user.getUserImageUrl(), image);
        ((TextView)headerView.findViewById(R.id.nav_header_user_name)).setText(user.name);
        headerView.findViewById(R.id.nav_header_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EntranceActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.isChecked()) {
                    drawerLayout.closeDrawers();
                    return true;
                }

                Fragment fragment = null;
                Bundle bundle = new Bundle();
                switch(item.getItemId()) {
                    case R.id.drawer_item_home:
                        fragment = ShotListFragment.createInstance();
                        bundle.putString("Token", m_token);
                        fragment.setArguments(bundle);
                        setTitle("Unsplash");
                        break;
                    case R.id.drawer_item_profile:
                        fragment = UserFragment.createInstance();
                        bundle.putString(UserActivity.USERINFO, m_user);
         //               bundle.putString(UserActivity.USERINFO, ModelUtils.toString(m_user, new TypeToken<User>(){}));
                        fragment.setArguments(bundle);
                        setTitle("Profile");
                        break;
                }

                drawerLayout.closeDrawers();

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    return true;
                }
                return false;
            }
        });

    }
}
