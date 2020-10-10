package com.example.mydribbble;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.mydribbble.BaseClass.HttpAsyncLoad;
import com.example.mydribbble.model.Collection;
import com.example.mydribbble.model.User;
import com.example.mydribbble.utils.ImageUtils;
import com.example.mydribbble.utils.ModelUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;

import okhttp3.Response;


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
        Unsplash.token = m_token;
        m_user = intent.getStringExtra(UserActivity.USERINFO);
        setTitle("Unsplash-Photos");

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
        Unsplash.username = user.username;
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

        new Thread(new Runnable() {
            private boolean collectionsRd;
            private Semaphore m1;
            @Override
            public void run() {
                collectionsRd = true;
                m1 = new Semaphore(1);
                while (true) {
                    try {
                        m1.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!collectionsRd) break;
                    new HttpAsyncLoad<Collection>(0) {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        public String createQuery() {
                            StringBuilder sb = new StringBuilder();
                            sb.append("https://api.unsplash.com/users/");
                            sb.append(Unsplash.username);
                            sb.append("/collections");
                            sb.append("?");
                            sb.append("access_token=");
                            sb.append(Unsplash.token);
                            sb.append("&page=" + Integer.toString(Unsplash.dict_collections.size() / CollectionListFragment.shotsPerPage + 1));
                            Log.d("ShotQuery", sb.toString());
                            return sb.toString();
                        }

                        @Override
                        protected void onPostExecute(Response response) {
                            List<Collection> shots = null;
                            try {
                                shots = this.parseResponse(response, new TypeToken<List<Collection>>() {});
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            super.onPostExecute(response);
                            for (int i = 0; i < shots.size(); i++) {
                                Unsplash.dict_collections.put(shots.get(i).id, shots.get(i).title);
                            }
                            if (shots.size() != CollectionListFragment.shotsPerPage) {
                                collectionsRd = false;
                            }
                            Log.d("ShotQuery", "reached here " + Unsplash.dict_collections.size());
                            m1.release();
                        }
                    }.execute();
                }
            }
        }).start();


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
                        setTitle("Unsplash-Photos");
                        break;
                    case R.id.drawer_item_profile:
                        fragment = UserFragment.createInstance();
                        bundle.putString(UserActivity.USERINFO, m_user);
         //               bundle.putString(UserActivity.USERINFO, ModelUtils.toString(m_user, new TypeToken<User>(){}));
                        fragment.setArguments(bundle);
                        setTitle("Profile");
                        break;
                    case R.id.drawer_item_collections:
                        fragment = CollectionListFragment.createInstance();
                        bundle.putString("Token", m_token);
                        fragment.setArguments(bundle);
                        setTitle("Unsplash-Collections");
                        break;
                    case R.id.drawer_create_collection:
                        fragment = CreateCollectionFragment.createInstance();
                        bundle.putString("Token", m_token);
                        fragment.setArguments(bundle);
                        setTitle("Create new collection");
                        break;
                    case R.id.drawer_search_photos:
                        fragment = SearchFragment.createInstance();
                        bundle.putString("Token", m_token);
                        bundle.putString("Type", "photos");
                        fragment.setArguments(bundle);
                        setTitle("Search Photos");
                        break;
                    case R.id.drawer_search_collections:
                        fragment = SearchFragment.createInstance();
                        bundle.putString("Token", m_token);
                        bundle.putString("Type", "collections");
                        fragment.setArguments(bundle);
                        setTitle("Search Collections");
                        break;
                    case R.id.drawer_search_users:
                        fragment = SearchFragment.createInstance();
                        bundle.putString("Token", m_token);
                        bundle.putString("Type", "users");
                        fragment.setArguments(bundle);
                        setTitle("Search Users");
                        break;
                }

                drawerLayout.closeDrawers();

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });

    }
}
