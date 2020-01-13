package com.example.pratush.docfinder.models;


import android.util.Log;

import com.example.pratush.docfinder.helpers.AsyncTaskCallback;
import com.example.pratush.docfinder.helpers.ConfigHelper;
import com.example.pratush.docfinder.helpers.TaskCallBack;
import com.example.pratush.docfinder.helpers.WebApiHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class DoclistModel
{
    public int catID;
    public int usrID;

    private AsyncTaskCallback callback;

    public DoclistModel ()
    {

    }
    public DoclistModel(JSONObject myData) throws JSONException
    {
        if (myData.has("catID")) this.catID = myData.getInt("catID");
        if (myData.has("usrID")) this.usrID = myData.getInt("usrID");
    }

    public void saveAsync(final AsyncTaskCallback callback) {

        this.callback = callback;

        try {
            JSONObject objJson = new JSONObject();

            objJson.put("catID", this.catID);
            objJson.put("usrID", this.usrID);


            WebApiHelper myHelper = new WebApiHelper(new TaskCallBack() {
                @Override
                public void onSuccess(Object response) throws JSONException {
                    super.onSuccess(response);

                    JSONObject apiResponse = new JSONObject(response.toString());
                    try {
                        DoclistModel obj = new DoclistModel(apiResponse);
                        callback.onSuccess(obj);
                    }
                    catch (JSONException e) {
                        Log.d("VS","DoclistModel"+ e.toString());
                    }
                }

                public void onError(Object response)
                {
                    super.onError(response);
                    callback.onError(response.toString());
                }
            });

            myHelper.execute(ConfigHelper.DataServiceUrl + "doccat/", "POST", objJson);
        } catch (Exception e)
        {
            Log.d("VS", e.toString());
        }
    }

    public void updateAsync(final AsyncTaskCallback callback) {

        this.callback = callback;

        try {
            JSONObject objJson = new JSONObject();

            objJson.put("catID", this.catID);
            objJson.put("usrID", this.usrID);


            WebApiHelper myHelper = new WebApiHelper(new TaskCallBack() {
                @Override
                public void onSuccess(Object response) throws JSONException {
                    super.onSuccess(response);

                    JSONObject apiResponse = new JSONObject(response.toString());
                    try {
                        DoclistModel obj = new DoclistModel(apiResponse);
                        callback.onSuccess(obj);
                    }
                    catch (JSONException e) {
                        Log.d("VS","DoclistModel"+ e.toString());
                    }
                }

                public void onError(Object response)
                {
                    super.onError(response);
                    callback.onError(response.toString());
                }
            });

            myHelper.execute(ConfigHelper.DataServiceUrl + "updatecat/"+usrID, "PUT", objJson);
        } catch (Exception e)
        {
            Log.d("VS", e.toString());
        }
    }


    public void getDocListAsync(final AsyncTaskCallback callback) {     // get list of doctors of a category

        this.callback = callback;

        try {
            JSONObject objJson = new JSONObject();

            objJson.put("catID", this.catID);
            objJson.put("usrID", this.usrID);


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

            myHelper.execute(ConfigHelper.DataServiceUrl + "getdoclist/"+catID, "GET", objJson);
        } catch (Exception e)
        {
            Log.d("VS", e.toString());
        }
    }
}
