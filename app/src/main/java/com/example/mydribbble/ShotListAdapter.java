package com.example.mydribbble;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydribbble.BaseActivity.SingleFragmentActivity;
import com.example.mydribbble.model.Shot;
import com.example.mydribbble.model.Shot2Detail;
import com.example.mydribbble.utils.ImageUtils;
import com.example.mydribbble.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ShotListAdapter extends RecyclerView.Adapter<ShotListViewHolder> {
    private static final int VIEW_TYPE_SHOT = 1;
    private static final int VIEW_TYPE_LOADING = 2;
    private List<Shot> data;
    private ShotListFragment shotListFragment;
    private LoadMoreTask loadMoreTask;
    private boolean showloading;
    private String m_token;
    @NonNull
    @Override
    public ShotListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(shotListFragment.getContext()).inflate(R.layout.list_item_loading, parent, false);
            return new ShotListViewHolder(view);
        } else {
            View view = LayoutInflater.from(shotListFragment.getContext()).inflate(R.layout.list_item_shot, parent, false);
            return new ShotListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ShotListViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == VIEW_TYPE_LOADING) {
            Log.d("ShotQuery", "loading");
            loadMoreTask.onLoadMore();
        } else {
            final ShotListViewHolder shotListViewHolder = (ShotListViewHolder) holder;
            final Shot shot = data.get(position);
            final String id = shot.id;
            shotListViewHolder.shot_user.setText(shot.user.name);
            shotListViewHolder.shot_like_count.setText(String.valueOf(shot.likes));
            shotListViewHolder.shot_download_count.setText(String.valueOf(shot.likes));
            shotListViewHolder.shot_view_count.setText(String.valueOf(shot.likes));
            ImageUtils.loadShotImage(shot.getImageUrl(), shotListViewHolder.image);
            Log.d("ShotQuery", "liked by the user" + shot.liked_by_user);
            //Log.d("ShotQuery", String.valueOf(shot.user.profile_image.get("small")));
            ImageUtils.loadShotImage(shot.getUserImageUrl(), shotListViewHolder.user_image);

            if (shot.liked_by_user) {
                shotListViewHolder.shot_dislike_action.setVisibility(View.GONE);
                shotListViewHolder.shot_like_action.setVisibility(View.VISIBLE);
            } else {
                shotListViewHolder.shot_like_action.setVisibility(View.GONE);
                shotListViewHolder.shot_dislike_action.setVisibility(View.VISIBLE);
            }


            shotListViewHolder.shot_like_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shotListViewHolder.shot_like_action.setVisibility(View.GONE);
                    shotListViewHolder.shot_dislike_action.setVisibility(View.VISIBLE);
                    shot.liked_by_user = !shot.liked_by_user;
                    ShotListFragment.likeAction(2, null, id);
                }
            });

            shotListViewHolder.shot_dislike_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shotListViewHolder.shot_dislike_action.setVisibility(View.GONE);
                    shotListViewHolder.shot_like_action.setVisibility(View.VISIBLE);
                    shot.liked_by_user =!shot.liked_by_user;
                    RequestBody postBody = new FormBody.Builder().add("id", shot.id).build();
                    ShotListFragment.likeAction(1, postBody, id);
                }
            });


            shotListViewHolder.cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(shotListFragment.getActivity(), ShotActivity.class);
                    String info = ModelUtils.toString(new Shot2Detail(m_token, id), new TypeToken<Shot2Detail>(){});
                    intent.putExtra(ShotActivity.KEY_SHOT, info);
                    shotListFragment.startActivityForResult(intent, shotListFragment.REQ_SHOT_CODE);
                }
            });
//            shotListViewHolder.cover.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(shotListFragment.getActivity(), ShotActivity.class);
//                    String body = ModelUtils.toString(shot, new TypeToken<Shot>() {});
//                    intent.putExtra(ShotActivity.KEY_SHOT, body);
//                    shotListFragment.startActivityForResult(intent, shotListFragment.REQ_SHOT_CODE);
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return showloading ? data.size() + 1 : data.size();
    }

    public ShotListAdapter(ShotListFragment shotListFragment, List<Shot> data, LoadMoreTask loadMoreTask, String token) {
        this.shotListFragment = shotListFragment;
        this.data = data;
        this.loadMoreTask = loadMoreTask;
        this.m_token = token;
    }

    public void setShowLoading(boolean showLoading) {
        this.showloading = showLoading;
    }

    public void setData(List<Shot> newData) {
        //data.clear();
        //data.addAll(newData);
        for (int i = 0; i < newData.size(); i++) {
            data.add(newData.get(i));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position < data.size() ? VIEW_TYPE_SHOT : VIEW_TYPE_LOADING;
    }

    public interface LoadMoreTask {
        void onLoadMore();
    }
}
