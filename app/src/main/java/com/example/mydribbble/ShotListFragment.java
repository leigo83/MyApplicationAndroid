package com.example.mydribbble;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.mydribbble.BaseClass.HttpAsyncLoad;
import com.example.mydribbble.BaseFragment.SingleFragment;
import com.example.mydribbble.model.Shot;
import com.example.mydribbble.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class ShotListFragment extends SingleFragment {
    public static final int REQ_SHOT_CODE = 100;
    public static final String SHOT_DATA_KEY = "SHOT_KEY";
    public static final String ShotLink = "https://api.unsplash.com/photos";
    public static final String CollectionLink = "https://api.unsplash.com/collections/";
    public static final String UserLink = "https://api.unsplash.com/users/";
    public static final String ShotLinkSingle = "https://api.dribbble.com/v2/shots/";
    public static final int shotsPerPage = 10;
    public RecyclerView recyclerView;
    List<Shot> dataShot = new ArrayList<>();
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
     //   recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
     //    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        final String token = getArguments().getString("Token");
        final String collection_id = getArguments().getString("CollectionId");
        final String userfeature = getArguments().getString("Userfeature");
        m_token = token;
        loadMoreTask = new ShotListAdapter.LoadMoreTask() {
            @Override
            public void onLoadMore() {
//               new LoadMoreTaskAsync(m_token).execute();
                new HttpAsyncLoad<Shot>(0) {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public String createQuery() {
                        StringBuilder sb = new StringBuilder ();
                        if (collection_id != null) {
                            sb.append(CollectionLink);
                            sb.append(collection_id);
                            sb.append("/photos");
                            sb.append("?");
                            sb.append("access_token=");
                            sb.append(token);
                            sb.append("&page=" + Integer.toString(dataShot.size() / shotsPerPage + 1));
                        } else if (userfeature != null) {
                            sb.append(UserLink);
                            sb.append(userfeature);
                            sb.append("?");
                            sb.append("access_token=");
                            sb.append(token);
                            sb.append("&page=" + Integer.toString(dataShot.size() / shotsPerPage + 1));
                        } else {
                                sb.append(ShotLink);
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
                        List<Shot> shots = null;
                        try {
                            shots = this.parseResponse(response, new TypeToken<List<Shot>>() {});
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        super.onPostExecute(response);
                        adapter.setData(shots);
                        adapter.setShowLoading(shots.size() == shotsPerPage);
                        if (shots.size() > 0) Log.d("ShotQuery","next" + shots.get(0).toString());
                    }
                }.execute();
            }
        };
        adapter = new ShotListAdapter(this, dataShot, loadMoreTask, m_token, Shot.class, ShotListViewHolder.class);
        adapter.setShowLoading(true);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    protected Fragment createFragment() {
        return new ShotListFragment();
    }

    public static Fragment createInstance() {
        return new ShotListFragment();
    }


    class LoadMoreTaskAsync extends AsyncTask<Void, Void, List<Shot>>  {
        public String m_token;

        LoadMoreTaskAsync(String token) {
            m_token = token;
        }

        @Override
        protected List<Shot> doInBackground(Void... voids) {
            StringBuilder sb = new StringBuilder ();
            sb.append(ShotLink);
            sb.append("?");
            sb.append("access_token=");
            sb.append(m_token);
            sb.append("&page=" + Integer.toString(dataShot.size() / shotsPerPage + 1));
            Log.d("ShotQuery", sb.toString());
            String query = Uri.parse(sb.toString()).buildUpon().build().toString();
            try {
                Response response = makeRequest(query);
                List<Shot> loadedData = parseResponse(response, new TypeToken<List<Shot>>() {});
                //          Log.d("num", Integer.toString(loadedData.size()));
                return loadedData;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Shot> shots) {
            super.onPostExecute(shots);
            adapter.setData(shots);
            adapter.setShowLoading(shots.size() == shotsPerPage);
        }
    }
/*
    class RemoveTask extends AsyncTask<Void, Void, Void> {
        public String m_token;
        public Shot m_shot;

        RemoveTask(String token, Shot shot) {
            m_token = token;
            m_shot = shot;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            StringBuilder sb = new StringBuilder ();
            sb.append(ShotLinkSingle);
            sb.append(Integer.toString(m_shot.id));
            sb.append("?");
            sb.append("access_token=");
            sb.append(m_token);
            String query = Uri.parse(sb.toString()).buildUpon().build().toString();
            try {
                Response response = deleteRequest(query, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class UpdateTask extends AsyncTask<Void, Void, Void> {
        public String m_token;
        public Shot m_shot;

        UpdateTask(String token, Shot shot) {
            m_token = token;
            m_shot = shot;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            StringBuilder sb = new StringBuilder ();
            sb.append(ShotLinkSingle);
            sb.append(Integer.toString(m_shot.id));
            sb.append("?");
            sb.append("access_token=");
            sb.append(m_token);
            FormBody.Builder formBody = new FormBody.Builder()
                    .add("description", m_shot.description)
                    .add("title", m_shot.title);
            for (int i = 0; i < m_shot.tags.size(); i++) {
                formBody.add("tags[]", m_shot.tags.get(i));
                Log.d("LeiFeng", m_shot.tags.get(i));
            }
            try {
                Response response = putRequest(sb.toString(), formBody.build());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQ_SHOT_CODE) {
            String result = data.getStringExtra(SHOT_DATA_KEY);
            Shot shot = ModelUtils.toObject(result, new TypeToken<Shot> () {});
            for (int i = 0; i < dataShot.size(); i++) {
                if (dataShot.get(i).id == shot.id) {
                    if (shot.images.size() == 0) {
                        dataShot.remove(i);
                        new RemoveTask(m_token, shot).execute();
                    } else {
                        dataShot.set(i, shot);
                        new UpdateTask(m_token, shot).execute();
                    }
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }*/
//    public void loadShots(final String token) {
//        new Thread(new Runnable () {
//            @Override
//            public void run() {
//                StringBuilder sb = new StringBuilder ();
////                sb.append(ShotLink);
////                sb.append("?");
////                sb.append("access_token=");
////                sb.append(token);
////                sb.append("&page=1");
////                Log.d("ShotQuery", sb.toString());
////                String query = Uri.parse(sb.toString()).buildUpon().build().toString();
////                try {
////                    Response response = makeRequest(query);
////                    List<Shot> loadedData = parseResponse(response, new TypeToken<List<Shot>>() {});
////                    adapter.setData(loadedData);
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//
//            }
//        }).start();
//    }

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

    public static void likeAction(int type, RequestBody postBody, final String id) {
        new HttpAsyncLoad<Void>(type, postBody) {
            @SuppressLint("StaticFieldLeak")
            @Override
            public String createQuery() {
                StringBuilder sb = new StringBuilder ();
                sb.append(ShotListFragment.ShotLink);
                sb.append("/");
                sb.append(id);
                sb.append("/");
                sb.append("like");
                sb.append("?");
                sb.append("access_token=");
                sb.append(m_token);
                Log.d("ShotQuery", sb.toString());
                return sb.toString();
            }
        }.execute();
    }

}
