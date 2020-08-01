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

    private List<Shot> data;
    private ShotListFragment shotListFragment;
    @NonNull
    @Override
    public ShotListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(shotListFragment.getContext()).inflate(R.layout.list_item_shot, parent, false);
        return new ShotListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShotListViewHolder holder, int position) {
        ShotListViewHolder shotListViewHolder = (ShotListViewHolder)holder;
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
                String body = ModelUtils.toString(shot, new TypeToken<Shot>() {});
                intent.putExtra(ShotActivity.KEY_SHOT, body);
                shotListFragment.startActivityForResult(intent, shotListFragment.REQ_SHOT_CODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ShotListAdapter(ShotListFragment shotListFragment, List<Shot> data) {
        this.shotListFragment = shotListFragment;
        this.data = data;
    }

    public void setData(List<Shot> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }
}
