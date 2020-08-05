package com.example.mydribbble;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydribbble.model.Shot;
import com.example.mydribbble.utils.ImageUtils;
import com.example.mydribbble.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ShotListAdapter extends RecyclerView.Adapter<ShotListViewHolder> {
    private static final int VIEW_TYPE_SHOT = 1;
    private static final int VIEW_TYPE_LOADING = 2;
    private List<Shot> data;
    private ShotListFragment shotListFragment;
    private LoadMoreTask loadMoreTask;
    private boolean showloading;
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
            loadMoreTask.onLoadMore();
        } else {
            ShotListViewHolder shotListViewHolder = (ShotListViewHolder) holder;
            final Shot shot = data.get(position);
            shotListViewHolder.shot_view_count.setText(String.valueOf(shot.title));
            shotListViewHolder.shot_like_count.setText(String.valueOf(shot.tags.get(0)));
            //   shotListViewHolder.shot_like_count.setText(String.valueOf(shot.description));
            shotListViewHolder.shot_bucket_count.setText(String.valueOf(shot.height));
//        shotListViewHolder.shot_view_count.setText(String.valueOf(shot.view_count));
//        shotListViewHolder.shot_like_count.setText(String.valueOf(shot.likes_count));
//        shotListViewHolder.shot_bucket_count.setText(String.valueOf(shot.buckets_count));
            ImageUtils.loadShotImage(shot, shotListViewHolder.image);
            shotListViewHolder.cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(shotListFragment.getActivity(), ShotActivity.class);
                    String body = ModelUtils.toString(shot, new TypeToken<Shot>() {
                    });
                    intent.putExtra(ShotActivity.KEY_SHOT, body);
                    shotListFragment.startActivityForResult(intent, shotListFragment.REQ_SHOT_CODE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return showloading ? data.size() + 1 : data.size();
    }

    public ShotListAdapter(ShotListFragment shotListFragment, List<Shot> data, LoadMoreTask loadMoreTask) {
        this.shotListFragment = shotListFragment;
        this.data = data;
        this.loadMoreTask = loadMoreTask;
    }

    public void setShowLoading(boolean showLoading) {
        this.showloading = showLoading;
    }

    public void setData(List<Shot> newData) {
        data.clear();
        data.addAll(newData);
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
