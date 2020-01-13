package com.example.pratush.docfinder.models;


import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import com.example.pratush.docfinder.helpers.AsyncTaskCallback;
import com.example.pratush.docfinder.helpers.ConfigHelper;
import com.example.pratush.docfinder.helpers.TaskCallBack;
import com.example.pratush.docfinder.helpers.WebApiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CategoryModel
{
    AsyncTaskCallback callback;
    public String cat;
    public int catID;

    public CategoryModel(){

    };

    public CategoryModel(JSONObject myData) throws JSONException {
        if (myData.has("cat")) this.cat = myData.getString("cat");
        if (myData.has("catID")) this.catID = Integer.parseInt(myData.getString("catID"));

    }

    // searching for category
    public void searchAsync(final AsyncTaskCallback callback) {
        this.callback = callback;

        try {
            JSONObject objJson = new JSONObject();
            WebApiHelper myHelper = new WebApiHelper(new TaskCallBack() {

                @Override
                public void onSuccess(Object response) throws JSONException {
                    super.onSuccess(response);

                    callback.onSuccess(response);
                }
                public void onError(Object response)
                {
                    super.onError(response);
                    callback.onError(response);
                }
            });

            myHelper.execute(ConfigHelper.DataServiceUrl + "specialist/"+cat , "GET", objJson);
        } catch (Exception e) {
            Log.d("VS", e.toString());
        }
    }
}
