package com.example.mydribbble;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydribbble.BaseActivity.SingleFragmentActivity;
import com.example.mydribbble.BaseFragment.SingleFragment;
import com.example.mydribbble.model.Collection;
import com.example.mydribbble.model.Shot;
import com.example.mydribbble.model.Shot2Detail;
import com.example.mydribbble.model.User;
import com.example.mydribbble.utils.ImageUtils;
import com.example.mydribbble.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class ShotListAdapter<T1, T2 extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T2> {
    private static final int VIEW_TYPE_SHOT = 1;
    private static final int VIEW_TYPE_LOADING = 2;
    private List<T1> data;
    private SingleFragment shotListFragment;
    private LoadMoreTask loadMoreTask;
    private boolean showloading;
    private String m_token;
    private final Class<T1> tpClass1;
    private final Class<T2> tpClass2;
    @NonNull
    @Override
    public T2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(shotListFragment.getContext()).inflate(R.layout.list_item_loading, parent, false);
            if (ShotListViewHolder.class == tpClass2) {
                return (T2)new ShotListViewHolder(view);
            } else if (CollectionListViewHolder.class == tpClass2) {
                return (T2)new CollectionListViewHolder(view);
            } else {
                return (T2)new UserListViewHolder(view);
            }
        } else {
            if (ShotListViewHolder.class == tpClass2) {
                View view = LayoutInflater.from(shotListFragment.getContext()).inflate(R.layout.list_item_shot, parent, false);
                return (T2)new ShotListViewHolder(view);
            } else if (CollectionListViewHolder.class == tpClass2) {
                View view = LayoutInflater.from(shotListFragment.getContext()).inflate(R.layout.list_item_collection, parent, false);
                return (T2)new CollectionListViewHolder(view);
            } else {
                View view = LayoutInflater.from(shotListFragment.getContext()).inflate(R.layout.list_item_user, parent, false);
                return (T2)new UserListViewHolder(view);
            }
        }
    }

    public ShotListAdapter(SingleFragment shotListFragment, List<T1> data, LoadMoreTask loadMoreTask, String token, Class<T1> type1, Class<T2> type2) {
        this.shotListFragment = shotListFragment;
        this.data = data;
        this.loadMoreTask = loadMoreTask;
        this.m_token = token;
        this.tpClass1 = type1;
        this.tpClass2 = type2;
    }


    @Override
    public void onBindViewHolder(@NonNull T2 holder, int position) {
        int type = getItemViewType(position);
        if (type == VIEW_TYPE_LOADING) {
            loadMoreTask.onLoadMore();
        } else {
            if (data.size() == 0) return;
            if (tpClass1 == Shot.class) {
                final ShotListViewHolder shotListViewHolder = (ShotListViewHolder) holder;
                final Shot shot = (Shot)data.get(position);
                final String id = shot.id;
                shotListViewHolder.shot_user.setText(shot.user.name);
                shotListViewHolder.shot_like_count.setText(String.valueOf(shot.likes));
                shotListViewHolder.shot_download_count.setText(String.valueOf(shot.likes));
                shotListViewHolder.shot_view_count.setText(String.valueOf(shot.likes));
                ImageUtils.loadShotImage(shot.getImageUrl(), shotListViewHolder.image);
                Log.d("ShotQuery", "liked by the user" + shot.liked_by_user);
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
                        shot.liked_by_user = !shot.liked_by_user;
                        RequestBody postBody = new FormBody.Builder().add("id", shot.id).build();
                        ShotListFragment.likeAction(1, postBody, id);
                    }
                });


                shotListViewHolder.cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(shotListFragment.getActivity(), ShotActivity.class);
                        String info = ModelUtils.toString(new Shot2Detail(m_token, id), new TypeToken<Shot2Detail>() {});
                        intent.putExtra(ShotActivity.KEY_SHOT, info);
                        intent.putExtra(CollectionListFragment.COLLECTIONID, ((ShotListFragment)shotListFragment).collection_id);
                        intent.putExtra(CollectionListFragment.COLLECTIONUSERNAME, ((ShotListFragment)shotListFragment).collection_username);
                        shotListFragment.startActivityForResult(intent, ((ShotListFragment)shotListFragment).REQ_SHOT_CODE);
                    }
                });
            } else if (tpClass1 == Collection.class){
                final CollectionListViewHolder collectionListViewHolder = (CollectionListViewHolder)holder;
                final Collection collection = (Collection)data.get(position);
                final String id = collection.id;
                collectionListViewHolder.title.setText(collection.title);
                collectionListViewHolder.user.setText(collection.user.username);
                ImageUtils.loadShotImage(collection.getImageUrl(), collectionListViewHolder.image);
                ImageUtils.loadShotImage(collection.getUserImageUrl(), collectionListViewHolder.userImage);
                collectionListViewHolder.cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(shotListFragment.getActivity(), ShotListActivity.class);
                        intent.putExtra(LoginFragment.ACCESS_TOKEN, m_token);
                        intent.putExtra(CollectionListFragment.COLLECTIONID, id);
                        intent.putExtra(CollectionListFragment.COLLECTIONUSERNAME, collection.user.username);
                        shotListFragment.startActivity(intent);
                    }
                });
                if (!collection.user.username.equals(Unsplash.username)) {
                    collectionListViewHolder.editButton.setVisibility(View.GONE);
                }
                collectionListViewHolder.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(shotListFragment.getActivity(), CollectionEditActivity.class);
                        intent.putExtra(CollectionListFragment.COLLECTIONINFO, ModelUtils.toString(collection, new TypeToken<Collection>() {}));
                        shotListFragment.startActivityForResult(intent, CollectionListFragment.REQ_CODE);
                    }
                });
            } else if (tpClass1 == User.class) {
                final UserListViewHolder userListViewHolder = (UserListViewHolder)holder;
                final User user = (User)data.get(position);
                userListViewHolder.username.setText("username: " + user.username);
                userListViewHolder.likes.setText("likes: " + user.total_likes);
                userListViewHolder.photos.setText("photos: " + user.total_photos);
                ImageUtils.loadShotImage(user.getUserImageUrl(), userListViewHolder.userImage);

                userListViewHolder.cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(shotListFragment.getActivity(), UserActivity.class);
                        intent.putExtra(UserActivity.USERINFO, ModelUtils.toString(user, new TypeToken<User>(){}));
                        shotListFragment.startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return showloading ? data.size() + 1 : data.size();
    }

    public void setShowLoading(boolean showLoading) {
        this.showloading = showLoading;
    }

    public void setData(List<T1> newData) {
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


//public class ShotListAdapter extends RecyclerView.Adapter<ShotListViewHolder> {
//    private static final int VIEW_TYPE_SHOT = 1;
//    private static final int VIEW_TYPE_LOADING = 2;
//    private List<Shot> data;
//    private ShotListFragment shotListFragment;
//    private LoadMoreTask loadMoreTask;
//    private boolean showloading;
//    private String m_token;
//    @NonNull
//    @Override
//    public ShotListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == VIEW_TYPE_LOADING) {
//            View view = LayoutInflater.from(shotListFragment.getContext()).inflate(R.layout.list_item_loading, parent, false);
//            return new ShotListViewHolder(view);
//        } else {
//            View view = LayoutInflater.from(shotListFragment.getContext()).inflate(R.layout.list_item_shot, parent, false);
//            return new ShotListViewHolder(view);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ShotListViewHolder holder, int position) {
//        int type = getItemViewType(position);
//        if (type == VIEW_TYPE_LOADING) {
//            Log.d("ShotQuery", "loading");
//            loadMoreTask.onLoadMore();
//        } else {
//            final ShotListViewHolder shotListViewHolder = (ShotListViewHolder) holder;
//            final Shot shot = data.get(position);
//            final String id = shot.id;
//            shotListViewHolder.shot_user.setText(shot.user.name);
//            shotListViewHolder.shot_like_count.setText(String.valueOf(shot.likes));
//            shotListViewHolder.shot_download_count.setText(String.valueOf(shot.likes));
//            shotListViewHolder.shot_view_count.setText(String.valueOf(shot.likes));
//            ImageUtils.loadShotImage(shot.getImageUrl(), shotListViewHolder.image);
//            Log.d("ShotQuery", "liked by the user" + shot.liked_by_user);
//            //Log.d("ShotQuery", String.valueOf(shot.user.profile_image.get("small")));
//            ImageUtils.loadShotImage(shot.getUserImageUrl(), shotListViewHolder.user_image);
//
//            if (shot.liked_by_user) {
//                shotListViewHolder.shot_dislike_action.setVisibility(View.GONE);
//                shotListViewHolder.shot_like_action.setVisibility(View.VISIBLE);
//            } else {
//                shotListViewHolder.shot_like_action.setVisibility(View.GONE);
//                shotListViewHolder.shot_dislike_action.setVisibility(View.VISIBLE);
//            }
//
//
//            shotListViewHolder.shot_like_action.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    shotListViewHolder.shot_like_action.setVisibility(View.GONE);
//                    shotListViewHolder.shot_dislike_action.setVisibility(View.VISIBLE);
//                    shot.liked_by_user = !shot.liked_by_user;
//                    ShotListFragment.likeAction(2, null, id);
//                }
//            });
//
//            shotListViewHolder.shot_dislike_action.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    shotListViewHolder.shot_dislike_action.setVisibility(View.GONE);
//                    shotListViewHolder.shot_like_action.setVisibility(View.VISIBLE);
//                    shot.liked_by_user =!shot.liked_by_user;
//                    RequestBody postBody = new FormBody.Builder().add("id", shot.id).build();
//                    ShotListFragment.likeAction(1, postBody, id);
//                }
//            });
//
//
//            shotListViewHolder.cover.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(shotListFragment.getActivity(), ShotActivity.class);
//                    String info = ModelUtils.toString(new Shot2Detail(m_token, id), new TypeToken<Shot2Detail>(){});
//                    intent.putExtra(ShotActivity.KEY_SHOT, info);
//                    shotListFragment.startActivityForResult(intent, shotListFragment.REQ_SHOT_CODE);
//                }
//            });
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return showloading ? data.size() + 1 : data.size();
//    }
//
//    public ShotListAdapter(ShotListFragment shotListFragment, List<Shot> data, LoadMoreTask loadMoreTask, String token) {
//        this.shotListFragment = shotListFragment;
//        this.data = data;
//        this.loadMoreTask = loadMoreTask;
//        this.m_token = token;
//    }
//
//    public void setShowLoading(boolean showLoading) {
//        this.showloading = showLoading;
//    }
//
//    public void setData(List<Shot> newData) {
//        //data.clear();
//        //data.addAll(newData);
//        for (int i = 0; i < newData.size(); i++) {
//            data.add(newData.get(i));
//        }
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position < data.size() ? VIEW_TYPE_SHOT : VIEW_TYPE_LOADING;
//    }
//
//    public interface LoadMoreTask {
//        void onLoadMore();
//    }
//}
