package com.example.mydribbble.BaseClass;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mydribbble.ShotListFragment;
import com.example.mydribbble.model.Shot;
import com.example.mydribbble.utils.ModelUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class HttpAsyncLoad<T> extends AsyncTask<Void, Void, Response> {
    private OkHttpClient client = new OkHttpClient();
    private int type;
    private RequestBody requestBody;

    public HttpAsyncLoad(int type) {
        this.type = type;
    }

    public HttpAsyncLoad(int type, RequestBody requestBody) {
        this.type = type;
        this.requestBody = requestBody;
    }

    public abstract String createQuery();

    @Override
    protected Response doInBackground(Void... voids) {
        String query = Uri.parse(createQuery()).buildUpon().build().toString();
        try {
            Response response = null;
            if (type == 0) {
                response = makeRequest(query);
            } else if (type == 1) {
                response = postRequest(query, this.requestBody);
            } else if (type == 2) {
                response = deleteRequest(query, null);
            }
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Response makeRequest(String query) throws IOException {
        Request request = new Request.Builder().url(query).build();
        Response response =  client.newCall(request).execute();
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

    public  <F> F parseResponse(Response response, TypeToken<F> typeToken) throws IOException {
        Log.d("ShotQuery", "it is from here");
        String responseString;
        responseString = response.body().string();
        return ModelUtils.toObject(responseString, typeToken);
    }

}
