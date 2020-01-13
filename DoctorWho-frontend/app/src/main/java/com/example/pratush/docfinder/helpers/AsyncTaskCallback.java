package com.example.pratush.docfinder.helpers;

import org.json.JSONException;


public interface AsyncTaskCallback
{
    void onSuccess(Object obj) throws JSONException;
    void onError(Object obj);
}