package com.seekingalpha.data.source;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.seekingalpha.data.Object;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.List;

public class ObjectsProvider implements ObjectsSource {

    private static final String TAG = ObjectsProvider.class.getName();
    public static final String BASE_URL = "http://54.202.245.89:3000/page/";
    private static ObjectsSource objectsSource;
    private final Context context;

    private ObjectsProvider(Context context) {
        this.context = context;
    }

    @Override
    public void getObjects(final LoadObjectsCallback callback, int pageNumber) {
        loadObjects(callback, pageNumber);
    }

    private void loadObjects(final LoadObjectsCallback callback, int pageNumber) {

        // https://developer.android.com/training/volley/request.html
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = BASE_URL + pageNumber;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");
                            Gson gson = new GsonBuilder().create();
                            Type collectionType = new TypeToken<Collection<Object>>(){}.getType();
                            List<Object> objects = gson.fromJson(jsonArray.toString(), collectionType);
                            callback.onObjectsLoaded(objects);
                        } catch (JSONException | JsonSyntaxException e) {
                            Log.d(TAG,"Error", e);
                            callback.onObjectsNotLoaded(ErrorReason.GENERAL);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse.statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                            callback.onObjectsNotLoaded(ErrorReason.LAST_PAGE);
                        } else {
                            callback.onObjectsNotLoaded(ErrorReason.GENERAL);
                        }
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    public static ObjectsSource getInstance(Context context) {
        if (objectsSource == null) {
            objectsSource = new ObjectsProvider(context);
        }
        return objectsSource;
    }
}
