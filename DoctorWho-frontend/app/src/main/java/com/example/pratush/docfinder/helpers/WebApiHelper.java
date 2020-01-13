package com.example.pratush.docfinder.helpers;


import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.pratush.docfinder.activities.NoInternetActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebApiHelper extends AsyncTask
{

    private AsyncTaskCallback callback;
    private String apiResponse;

    public WebApiHelper(TaskCallBack callback){
        this.callback = callback;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            String apiUrl = (String) objects[0];
            String httpMethod = (String) objects[1];
            JSONObject payload = (JSONObject) objects[2];

            if(httpMethod.equals("POST")||httpMethod.equals("PUT")) {
                URL objURL = new URL(apiUrl);
                HttpURLConnection objHttpCon = (HttpURLConnection) objURL.openConnection();
                objHttpCon.setRequestMethod(httpMethod);
                objHttpCon.setDoInput(true);
                objHttpCon.setDoOutput(true);
                objHttpCon.setRequestProperty("content-type","application/json");
                objHttpCon.setFixedLengthStreamingMode(payload.toString().length());

                byte[] data = payload.toString().getBytes();
                OutputStream  os = objHttpCon.getOutputStream();
                os.write(data);
                if (objHttpCon.getResponseCode() != 200 && objHttpCon.getResponseCode() !=201) {
                    callback.onError(objHttpCon.getResponseCode());
                } else {
                    BufferedReader objBufferReader = new BufferedReader(new InputStreamReader((objHttpCon.getInputStream())));
                    apiResponse = objBufferReader.readLine();
                    callback.onSuccess(apiResponse);
                }

                objHttpCon.disconnect();
            }

            else if(httpMethod.equals("GET"))
            {
                URL objURL = new URL(apiUrl);
                HttpURLConnection objHttpCon = (HttpURLConnection) objURL.openConnection();
                objHttpCon.setRequestMethod(httpMethod);

                if (objHttpCon.getResponseCode() != 200 && objHttpCon.getResponseCode() !=201) {
                    callback.onError(objHttpCon.getResponseCode());
                } else {
                    BufferedReader objBufferReader = new BufferedReader(new InputStreamReader((objHttpCon.getInputStream())));
                    apiResponse = objBufferReader.readLine();
                    callback.onSuccess(apiResponse);
                }

                objHttpCon.disconnect();

            } else if (httpMethod.equals("DELETE")) {
                URL objURL = new URL(apiUrl);
                HttpURLConnection objHttpCon = (HttpURLConnection) objURL.openConnection();
                objHttpCon.setRequestMethod(httpMethod);

                if (objHttpCon.getResponseCode() != 200 && objHttpCon.getResponseCode() != 201 && objHttpCon.getResponseCode() != 204) {
                    callback.onError(objHttpCon.getResponseCode());
                } else {
                    apiResponse = "deleted";
                    callback.onSuccess(apiResponse);
                }

                objHttpCon.disconnect();

            }


        } catch (Exception e) {
            Log.d("VS","here "+e.toString());
            String error = "ConnectionError";
            callback.onError(error);
        }

        return apiResponse;
    }

}
