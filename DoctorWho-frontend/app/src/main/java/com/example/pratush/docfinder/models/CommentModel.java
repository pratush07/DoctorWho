package com.example.pratush.docfinder.models;

import android.util.Log;

import com.example.pratush.docfinder.helpers.AsyncTaskCallback;
import com.example.pratush.docfinder.helpers.ConfigHelper;
import com.example.pratush.docfinder.helpers.TaskCallBack;
import com.example.pratush.docfinder.helpers.WebApiHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class CommentModel
{
    public int com_from;
    public int com_to;
    public String com;
    public String com_type;
    AsyncTaskCallback callback;

   //storing a comment
    public void comment_store(final AsyncTaskCallback callback) {
        this.callback = callback;

        try {
            JSONObject objJson = new JSONObject();
            objJson.put("com_from",com_from);
            objJson.put("com_to",com_to);
            objJson.put("com",com);
            objJson.put("com_type",com_type);

            WebApiHelper myHelper = new WebApiHelper(new TaskCallBack() {

                @Override
                public void onSuccess(Object response) throws JSONException
                {
                    super.onSuccess(response);
                    callback.onSuccess(response);

                }
                public void onError(Object response)
                {
                    super.onError(response);
                    callback.onError(response);
                }
            });

            myHelper.execute(ConfigHelper.DataServiceUrl+"comment/", "POST", objJson);
        } catch (Exception e) {
            Log.d("VS", e.toString());
        }
    }
    public void comment_retrieve(final AsyncTaskCallback callback) {
        this.callback = callback;

        try {
            JSONObject objJson = new JSONObject();
            objJson.put("com_from",com_from);
            objJson.put("com_to",com_to);
            objJson.put("com",com);
            objJson.put("com_type",com_type);

            WebApiHelper myHelper = new WebApiHelper(new TaskCallBack() {

                @Override
                public void onSuccess(Object response) throws JSONException
                {
                    super.onSuccess(response);
                    callback.onSuccess(response);

                }
                public void onError(Object response)
                {
                    super.onError(response);
                    callback.onError(response);
                }
            });

            myHelper.execute(ConfigHelper.DataServiceUrl+"comment/"+com_to, "GET", objJson);
        } catch (Exception e) {
            Log.d("VS", e.toString());
        }
    }
}
