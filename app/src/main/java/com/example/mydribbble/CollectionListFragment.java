package com.example.mydribbble;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.example.mydribbble.model.Shot;
import com.example.mydribbble.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class CollectionListFragment extends SingleFragment {
    public static String COLLECTIONID = "collection_id";
    public static String COLLECTIONINFO = "collection_info";
    public static final int REQ_SHOT_CODE = 100;
    public static final String SHOT_DATA_KEY = "SHOT_KEY";
    public static final String ShotLink = "https://api.unsplash.com/collections";
    public static final String UserLink = "https://api.unsplash.com/users/";
    public static final int shotsPerPage = 10;
    public static int REQ_CODE = 10002;
    public RecyclerView recyclerView;
    List<Collection> dataShot = new ArrayList<>();
    public ShotListAdapter adapter;
    private static OkHttpClient client = new OkHttpClient();
    public static String m_token;
    private ShotListAdapter.LoadMoreTask loadMoreTask;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final String token = getArguments().getString("Token");
        final String userfeature = getArguments().getString("Userfeature");
        m_token = token;
        loadMoreTask = new ShotListAdapter.LoadMoreTask() {
            @Override
            public void onLoadMore() {
//               new LoadMoreTaskAsync(m_token).execute();
                new HttpAsyncLoad<Collection>(0) {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public String createQuery() {
                        StringBuilder sb = new StringBuilder ();
                        if (userfeature == null) {
                            sb.append(ShotLink);
                            sb.append("?");
                            sb.append("access_token=");
                            sb.append(token);
                            sb.append("&page=" + Integer.toString(dataShot.size() / shotsPerPage + 1));
                        } else {
                            sb.append(UserLink);
                            sb.append(userfeature);
                            sb.append("?");
                            sb.append("access_token=");
                            sb.append(token);
                            sb.append("&page=" + Integer.toString(dataShot.size() / shotsPerPage + 1));
                        }
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
                        adapter.setData(shots);
                        adapter.setShowLoading(shots.size() == shotsPerPage);
                    }
                }.execute();
            }
        };
        adapter = new ShotListAdapter(this, dataShot, loadMoreTask, m_token, Collection.class, CollectionListViewHolder.class);
        adapter.setShowLoading(true);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("ShotQuery", requestCode + "," + resultCode);
        if ((resultCode == RESULT_OK) && (requestCode == REQ_CODE))
        {
            String [] info = data.getStringExtra("ID").split(",");
            String id = info[0];
            String delete = info[1];
            if (delete.equals("1")) {
                for (int i = 0; i < dataShot.size(); i++) {
                    if (dataShot.get(i).id.equals(id)) {
                        Log.d("ShotQuery",  "hit");
                        dataShot.remove(i);
                        break;
                    }
                }
            } else {
                for (int i = 0; i < dataShot.size(); i++) {
                    if (dataShot.get(i).id.equals(id)) {
                        Log.d("ShotQuery", "hit");
                        dataShot.get(i).title = data.getStringExtra("title");
                        dataShot.get(i).description = data.getStringExtra("description");
                        break;
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected Fragment createFragment() {
        return new CollectionListFragment();
    }

    public static Fragment createInstance() {
        return new CollectionListFragment();
    }

    public Response makeRequest(String query) throws IOException {
        Request request = new Request.Builder().url(query).build();
        Response response =  client.newCall(request).execute();
        Log.d("ShotQuery", response.toString());
        return response;
    }

    public Response deleteRequest(String url, RequestBody requestBody) throws IOException {
        Request request = new Request.Builder().url(url).delete().build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public Response putRequest(String url, RequestBody requestBody) throws IOException {
        Request request = new Request.Builder().url(url).put(requestBody).build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public Response postRequest(String url, RequestBody requestBody) throws IOException {
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static <T> T parseResponse(Response response, TypeToken<T> typeToken) throws IOException {
        String responseString;
        responseString = response.body().string();
        return ModelUtils.toObject(responseString, typeToken);
    }
}
