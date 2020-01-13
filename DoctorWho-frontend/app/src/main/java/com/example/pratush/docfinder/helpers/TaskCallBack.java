package com.example.pratush.docfinder.helpers;

import android.util.Log;

import org.json.JSONException;

public class TaskCallBack implements AsyncTaskCallback
{
    @Override
    public void onSuccess(Object apiResponse) throws JSONException {

        Log.d("VS", "success" + apiResponse.toString());
    }
    @Override
    public void onError(Object apiResponse)
    {
        Log.e("VS","error"+ apiResponse.toString());
    }
}