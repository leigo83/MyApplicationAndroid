package com.example.mydribbble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mydribbble.BaseClass.HttpAsyncLoad;
import com.example.mydribbble.BaseFragment.SingleFragment;
import com.example.mydribbble.model.Collection;
import com.example.mydribbble.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateCollectionFragment extends SingleFragment {
    private String m_token;
    private String collectionLink = "https://api.unsplash.com/collections";
    private boolean updated = false;
    private String info;
    private String cur_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.collection_create, container, false);
         final EditText title = view.findViewById(R.id.creatColleiciton_title);
         final EditText descript = view.findViewById(R.id.createCollection_descript);
         Button submit = view.findViewById(R.id.createCollection_submit);
         Button delete = view.findViewById(R.id.deleteCollection);
         m_token = getArguments().getString("Token");
         info = getArguments().getString("INFO");

         if (info == null) {
             updated = false;
             delete.setVisibility(View.GONE);
         } else {
             updated = true;
             delete.setVisibility(View.VISIBLE);
             Collection cur_collection = ModelUtils.toObject(info, new TypeToken<Collection>() {});
             title.setText(cur_collection.title);
             descript.setText(cur_collection.description);
             cur_id = cur_collection.id;
         }

         delete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 new HttpAsyncLoad<Collection>(2, null) {

                     @Override
                     protected void onPostExecute(Response response) {
                         Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_LONG).show();
                         FragmentManager fm = getFragmentManager();
                         if (fm.getBackStackEntryCount() > 0) {
                             Log.i("ShotQuery", "popping backstack");
                             fm.popBackStack();
                         } else {
                             Intent resultIntent = new Intent(getActivity(), CollectionListActivity.class);
                             resultIntent.putExtra("ID", cur_id + "," + "1");
                             getActivity().setResult(Activity.RESULT_OK,  resultIntent);
                             getActivity().finish();
                         }
                     }

                     @Override
                     public String createQuery() {
                         StringBuilder sb = new StringBuilder();
                         sb.append(collectionLink);
                         sb.append("/");
                         sb.append(cur_id);
                         sb.append("?");
                         sb.append("access_token=" + m_token);
                         return sb.toString();
                     }
                 }.execute();
             }
         });

         submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 final RequestBody postBody = new FormBody.Builder()
                         .add("title", title.getText().toString())
                         .add("description", descript.getText().toString()).build();

                 new HttpAsyncLoad<Collection>(updated ? 3 : 1, postBody) {
                     @Override
                     protected void onPostExecute(Response response) {
                         super.onPostExecute(response);
                         try {
                             Collection collection = this.parseResponse(response, new TypeToken<Collection>() {});
                             cur_id = collection.id;
                         } catch (IOException e) {
                             e.printStackTrace();
                         }

                         if (!updated) {
                             Toast.makeText(getContext(), "Created!", Toast.LENGTH_LONG).show();
                         } else {
                             Toast.makeText(getContext(), "Updated!", Toast.LENGTH_LONG).show();
                         }
                         updated = true;
                         FragmentManager fm = getFragmentManager();
                         if (fm.getBackStackEntryCount() > 0) {
                             Log.i("ShotQuery", "popping backstack");
                             fm.popBackStack();
                         } else {
                             Intent resultIntent = new Intent(getActivity(), CollectionListActivity.class);
                             resultIntent.putExtra("ID", cur_id + "," + "0");
                             resultIntent.putExtra("title", title.getText().toString());
                             resultIntent.putExtra("description", descript.getText().toString());
                             getActivity().setResult(Activity.RESULT_OK, resultIntent);
                             getActivity().finish();
                         }
                     }

                     @Override
                     public String createQuery() {
                         StringBuilder sb = new StringBuilder();
                         if (!updated) {
                             sb.append(collectionLink);
                             sb.append("?");
                             sb.append("access_token=");
                             sb.append(m_token);
                         } else {
                             sb.append(collectionLink + "/");
                             sb.append(cur_id);
                             sb.append("?");
                             sb.append("access_token=");
                             sb.append(m_token);
                         }
                         return sb.toString();
                     }
                 }.execute();
             }
         });
         return view;
    }

    @Override
    protected Fragment createFragment() {
        return new CreateCollectionFragment();
    }

    public static Fragment createInstance() {
        return new CreateCollectionFragment();
    }


}
