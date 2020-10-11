package com.example.mydribbble;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.mydribbble.BaseClass.HttpAsyncLoad;
import com.example.mydribbble.BaseFragment.SingleFragment;
import com.example.mydribbble.model.Shot;
import com.example.mydribbble.model.Shot2Detail;
import com.example.mydribbble.model.ShotDetail;
import com.example.mydribbble.model.User;
import com.example.mydribbble.utils.ImageUtils;
import com.example.mydribbble.utils.ModelUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.File;

import static android.app.Activity.RESULT_OK;

public class ShotFragment extends SingleFragment {
    public static final String ShotLink = "https://api.unsplash.com/photos/";
    public static final String UserLink = "https://api.unsplash.com/users/";
    public static final String CollectionLink = "https://api.unsplash.com/collections/";
    public ShotDetail shotDetail;
    public String m_token;
    private String collectionId;
    private String photoId;
    private Context context = this.getContext();
    @Override
    protected Fragment createFragment() {
        return new ShotFragment();
    }

    public static Fragment createInstance() {
        return new ShotFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.shot_details, container, false);
        final Shot2Detail info = ModelUtils.toObject(getArguments().getString(ShotActivity.KEY_SHOT), new TypeToken<Shot2Detail>() {});
        m_token = info.token;
        HttpAsyncLoad<ShotDetail> getInfo = new HttpAsyncLoad<ShotDetail>(0) {
            @Override
            public String createQuery() {
                StringBuilder sb = new StringBuilder ();
                sb.append(ShotLink);
                sb.append(info.id);
                sb.append("?");
                sb.append("access_token=");
                sb.append(info.token);
                Log.d("ShotQuery", sb.toString());
                return sb.toString();
            }

            @Override
            protected void onPostExecute(Response response) {
                super.onPostExecute(response);
                try {
                    shotDetail = (ShotDetail) this.parseResponse(response, new TypeToken<ShotDetail>() {});
                    setViews(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                super.onPostExecute(response);
            }
        };
        getInfo.execute();

     /*   title_edit.setText(shot.title);
        descript_edit.setText(shot.description);
        StringBuilder sb = new StringBuilder ();
        for (int i = 0; i < shot.tags.size(); i++) {
            sb.append(shot.tags.get(i));
            sb.append(" ");
        }
        tag_edit.setText(sb.toString());
        Button saveButton = view.findViewById(R.id.save_change);
        Button deleteButton = view.findViewById(R.id.delete);
        title_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shot.title = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        descript_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shot.description = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tag_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String [] data = s.toString().split("\\s+");
                shot.tags.clear();
                for (int i = 0; i < data.length; i++) {
                    shot.tags.add(data[i]);
                    Log.d("LeiValue", data[i]);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = ModelUtils.toString(shot, new TypeToken<Shot>() {});
                updateData(body);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shot.images.clear();
                String body = ModelUtils.toString(shot, new TypeToken<Shot>() {});
                updateData(body);
            }
        });*/
        return view;
    }

    private void setViews(View view) {
        final SimpleDraweeView image = view.findViewById(R.id.shot_image);
        final TextView likes = view.findViewById(R.id.likes);
        final TextView dislikes = view.findViewById(R.id.dislikes);
        TextView username = view.findViewById(R.id.username);
        TextView download = view.findViewById(R.id.shotdetail_download);
        TextView location = view.findViewById(R.id.location);
        TextView name = view.findViewById(R.id.name);
        TextView description = view.findViewById(R.id.description);
        TextView camera = view.findViewById(R.id.camera);
        TextView ownedPhotos = view.findViewById(R.id.ownedPhotos);
        Button delete = view.findViewById(R.id.remove_photo);
        LinearLayout group = view.findViewById(R.id.add_photo_group);
        String collectionUserName = getArguments().getString(CollectionListFragment.COLLECTIONUSERNAME);
        collectionId = getArguments().getString(CollectionListFragment.COLLECTIONID);
        if (collectionUserName != null && collectionUserName.equals(Unsplash.username)) {
            group.setVisibility(View.GONE);
            delete.setVisibility(View.VISIBLE);
        }

        Log.d("ShotQuery", String.valueOf(shotDetail.downloads));
        if (shotDetail.liked_by_user) {
            likes.setVisibility(View.VISIBLE);
            dislikes.setVisibility(View.GONE);
            likes.setText(String.valueOf(shotDetail.likes));
        } else {
            likes.setVisibility(View.GONE);
            dislikes.setVisibility(View.VISIBLE);
            dislikes.setText(String.valueOf(shotDetail.likes));
        }
        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likes.setVisibility(View.GONE);
                dislikes.setVisibility(View.VISIBLE);
                dislikes.setText(String.valueOf(shotDetail.likes));
                ShotListFragment.likeAction(2, null, shotDetail.id);
            }
        });
        dislikes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dislikes.setVisibility(View.GONE);
                likes.setVisibility(View.VISIBLE);
                likes.setText(String.valueOf(shotDetail.likes));
                RequestBody postBody = new FormBody.Builder().add("id", shotDetail.id).build();
                ShotListFragment.likeAction(1, postBody, shotDetail.id);
            }
        });
        download.setText(String.valueOf(shotDetail.downloads));
        SpannableString content = new SpannableString(shotDetail.user.username);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        username.setText(content);
        username.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new HttpAsyncLoad<User>(0) {
                    @Override
                    protected void onPostExecute(Response response) {
                        super.onPostExecute(response);
                        try {
                            User user = (User)this.parseResponse(response, new TypeToken<User>(){});
                            Intent intent = new Intent(getContext(), UserActivity.class);
                            intent.putExtra(UserActivity.USERINFO, ModelUtils.toString(user, new TypeToken<User>(){}));
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public String createQuery() {
                        StringBuilder sb = new StringBuilder ();
                        sb.append(UserLink);
                        sb.append(shotDetail.user.username);
                        sb.append("?");
                        sb.append("access_token=");
                        sb.append(m_token);
                        return sb.toString();
                    }
                }.execute();
            }
        });
        name.setText("Name: " + shotDetail.user.name);
        description.setText("Description: " + shotDetail.description);
        ownedPhotos.setText("OwnedPhotos: " + shotDetail.user.total_photos);
        if (shotDetail.location.city != null && shotDetail.location.country != null) {
            location.setText("Location: " + shotDetail.location.city + ", " + shotDetail.location.country);
        }
        if (shotDetail.exif.make != null && shotDetail.exif.model != null) {
            camera.setText("Camera Make/Model: " + shotDetail.exif.make + "/" + shotDetail.exif.model);
        }


        ImageUtils.loadShotImage(shotDetail.getImageUrl(), image);

        Spinner spin = view.findViewById(R.id.spinner);
        final List<String> myCollections = new ArrayList<>();
        final HashMap<Integer, String> mapping = new HashMap<> ();
        myCollections.add("");
        for (HashMap.Entry<String, String> entry : Unsplash.dict_collections.entrySet()) {
            String key = entry.getKey();
            mapping.put(myCollections.size(), key);
            myCollections.add(entry.getValue());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, myCollections);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                collectionId = mapping.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button submit = view.findViewById(R.id.add_photo);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoId = shotDetail.id;
                RequestBody postBody = new FormBody.Builder()
                        .add("access_token", m_token)
                        .add("collection_id", collectionId)
                        .add("photo_id", photoId).build();
                Log.d("ShotQuery", postBody.toString());
                new HttpAsyncLoad<User>(1, postBody) {
                    @Override
                    protected void onPostExecute(Response response) {
       //                 Toast.makeText(context, "Added!", Toast.LENGTH_LONG);
                    }

                    @Override
                    public String createQuery() {
                        StringBuilder sb = new StringBuilder ();
                        sb.append(CollectionLink);
                        sb.append(collectionId);
                        sb.append("/add");
                        Log.d("ShotQuery", sb.toString());
                        return sb.toString();
                    }
                }.execute();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoId = shotDetail.id;
                new HttpAsyncLoad<User>(2) {
                    @Override
                    protected void onPostExecute(Response response) {
                        //                 Toast.makeText(context, "Added!", Toast.LENGTH_LONG);
                        getActivity().finish();
                    }

                    @Override
                    public String createQuery() {
                        StringBuilder sb = new StringBuilder ();
                        sb.append(CollectionLink);
                        sb.append(collectionId);
                        sb.append("/remove");
                        sb.append("?");
                        sb.append("access_token=" + m_token);
                        sb.append("&collection_id=" + collectionId);
                        sb.append("&photo_id=" + photoId);
                        Log.d("ShotQuery", sb.toString());
                        return sb.toString();
                    }
                }.execute();
            }
        });

        Button downloadImage = view.findViewById(R.id.download_image);
        downloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... voids) {
                        Bitmap mIcon11 = null;
                        try {
                            InputStream in = new java.net.URL(shotDetail.getImageUrl()).openStream();
                            mIcon11 = BitmapFactory.decodeStream(in);
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage());
                            e.printStackTrace();
                        }
                        return mIcon11;
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        MediaStore.Images.Media.insertImage(getContext().getApplicationContext().getContentResolver(), bitmap, shotDetail.description, null);
                    }
                }.execute();
            }
        });

        Button shareImage = view.findViewById(R.id.share_image);
        final Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... voids) {
                        Bitmap mIcon11 = null;
                        try {
                            InputStream in = new java.net.URL(shotDetail.getImageUrl()).openStream();
                            File file = new File(getActivity().getExternalCacheDir(),"temp.jpg");
                            mIcon11 = BitmapFactory.decodeStream(in);
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage());
                            e.printStackTrace();
                        }
                        return mIcon11;
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        File file = new File(getContext().getExternalCacheDir(),"temp.jpg");
                        FileOutputStream fOut = null;
                        try {
                            fOut = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                            fOut.flush();
                            fOut.close();
                            file.setReadable(true, false);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Uri imageUri = FileProvider.getUriForFile(
                                getContext(),
                                getActivity().getPackageName() + ".provider",
                                file);
                        share.putExtra(Intent.EXTRA_STREAM, imageUri);
                        startActivity(Intent.createChooser(share, "Select"));
                    }
                }.execute();
            }
        });

    }

    private void updateData(String body) {
        Intent intent = new Intent(getActivity(), ShotListActivity.class);
        intent.putExtra(ShotListFragment.SHOT_DATA_KEY, body);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }
}
