package com.example.mydribbble;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydribbble.BaseClass.HttpAsyncLoad;
import com.example.mydribbble.BaseFragment.SingleFragment;
import com.example.mydribbble.model.Collection;
import com.example.mydribbble.model.CollectionSearchResult;
import com.example.mydribbble.model.User;
import com.example.mydribbble.model.UserSearchResult;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class UserListFragment extends SingleFragment {
    public RecyclerView recyclerView;
    public String queryInfo = null;
    List<User> dataShot = new ArrayList<>();
    public static final int shotsPerPage = 10;
    private ShotListAdapter.LoadMoreTask loadMoreTask;
    public String m_token;
    public ShotListAdapter adapter;

    @Override
    protected Fragment createFragment() {
        return new UserListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final String token = getArguments().getString("Token");
        final String userfeature = getArguments().getString("Userfeature");
        m_token = token;
        queryInfo = getArguments().getString(SearchFragment.QUERYINFO);
        loadMoreTask = new ShotListAdapter.LoadMoreTask() {
            @Override
            public void onLoadMore() {
//               new LoadMoreTaskAsync(m_token).execute();
                new HttpAsyncLoad<User>(0) {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public String createQuery() {
                        StringBuilder sb = new StringBuilder ();
                        sb.append(queryInfo);
                        sb.append("&page=" + Integer.toString(dataShot.size() / shotsPerPage + 1));
                        Log.d("ShotQuery", sb.toString());
                        return sb.toString();
                    }

                    @Override
                    protected void onPostExecute(Response response) {
                        List<User> shots = null;
                        try {
                            if (queryInfo != null) {
                                UserSearchResult temp = this.parseResponse(response, new TypeToken<UserSearchResult>() {});
                                shots = temp.results;
                            } else {
                                shots = this.parseResponse(response, new TypeToken<List<User>>() {});
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        super.onPostExecute(response);
                        adapter.setData(shots);
                        adapter.setShowLoading(shots.size() == shotsPerPage);
                    }
                }.execute();
            }
        };
        adapter = new ShotListAdapter(this, dataShot, loadMoreTask, m_token, User.class, UserListViewHolder.class);
        adapter.setShowLoading(true);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
