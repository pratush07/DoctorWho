package com.example.pratush.docfinder.models;


import android.util.Log;

import com.example.pratush.docfinder.helpers.AsyncTaskCallback;
import com.example.pratush.docfinder.helpers.ConfigHelper;
import com.example.pratush.docfinder.helpers.TaskCallBack;
import com.example.pratush.docfinder.helpers.WebApiHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class RateModel
{
    public int rate_from;
    public int rate_to;
    public double rate;


    private AsyncTaskCallback callback;

    public RateModel ()
    {

    }
    public RateModel(JSONObject myData) throws JSONException
    {
        if (myData.has("rate_from")) this.rate_from = myData.getInt("rate_from");
        if (myData.has("rate_to")) this.rate_to = myData.getInt("rate_to");
        if (myData.has("rate")) this.rate = myData.getDouble("rate");
    }

    public void saveRateAsync(final AsyncTaskCallback callback) {

        this.callback = callback;

        try {
            JSONObject objJson = new JSONObject();

            objJson.put("rate_from", this.rate_from);
            objJson.put("rate_to", this.rate_to);
            objJson.put("rate", this.rate);


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

            myHelper.execute(ConfigHelper.DataServiceUrl + "ratedoc/"+rate_to, "POST", objJson);
        } catch (Exception e)
        {
            Log.d("VS", e.toString());
        }
    }

    public void getAverageRate(final AsyncTaskCallback callback) {

        this.callback = callback;

        try {
            JSONObject objJson = new JSONObject();




            WebApiHelper myHelper = new WebApiHelper(new TaskCallBack() {
                @Override
                public void onSuccess(Object response) throws JSONException {
                    super.onSuccess(response);

                    JSONObject apiResponse = new JSONObject(response.toString());
                    try {
                        DoclistModel obj = new DoclistModel(apiResponse);   //because a doctor object with updated rate will be returned
                        callback.onSuccess(obj);
                    }
                    catch (JSONException e) {
                        Log.d("VS", "RateModel" + e.toString());
                    }
                }

                public void onError(Object response)
                {
                    super.onError(response);
                    callback.onError(response.toString());
                }
            });

            myHelper.execute(ConfigHelper.DataServiceUrl + "averagerate/"+rate_to, "GET", objJson);
        } catch (Exception e)
        {
            Log.d("VS", e.toString());
        }
    }
}
