package com.example.pratush.docfinder.models;


import android.util.Log;

import com.example.pratush.docfinder.helpers.AsyncTaskCallback;
import com.example.pratush.docfinder.helpers.ConfigHelper;
import com.example.pratush.docfinder.helpers.TaskCallBack;
import com.example.pratush.docfinder.helpers.WebApiHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class UserModel
{
    public int ID;
    public String FirstName;
    public String LastName;
    public String EmailAddress;
    public String Gender;
    public String ProfilePic;

    private AsyncTaskCallback callback;

    public UserModel ()
    {

    }
    public UserModel(JSONObject myData) throws JSONException {
        if (myData.has("id")) this.ID = Integer.parseInt(myData.getString("id"));
        if (myData.has("firstName")) this.FirstName = myData.getString("firstName");
        if (myData.has("lastName")) this.LastName = myData.getString("lastName");
        if (myData.has("email")) this.EmailAddress = myData.getString("email");
        if (myData.has("gender")) this.Gender = myData.getString("gender");
        if (myData.has("profilePic")) this.ProfilePic = myData.getString("profilePic");
    }


    // Save User
    public void saveAsync(final AsyncTaskCallback callback) {

        this.callback = callback;

        try {
            JSONObject objJson = new JSONObject();

            objJson.put("profilePic", this.ProfilePic);
            objJson.put("firstName", this.FirstName);
            objJson.put("lastName", this.LastName);
            objJson.put("email", this.EmailAddress);
            objJson.put("gender", this.Gender);

            WebApiHelper myHelper = new WebApiHelper(new TaskCallBack() {
                @Override
                public void onSuccess(Object response) throws JSONException {
                    super.onSuccess(response);

                    JSONObject apiResponse = new JSONObject(response.toString());
                    try {
                        UserModel obj = new UserModel(apiResponse);
                        Log.d("VS",""+obj.ID);
                        callback.onSuccess(obj);
                    }
                    catch (JSONException e) {
                        Log.d("VS","UserModel"+ e.toString());
                    }
                }

                public void onError(Object response)
                {
                    super.onError(response);
                    callback.onError(response.toString());
                }
            });

            myHelper.execute(ConfigHelper.DataServiceUrl + "user/", "POST", objJson);
        } catch (Exception e)
        {
            Log.d("VS", e.toString());
        }
    }

    public void loadAsync(final int id, final AsyncTaskCallback callback) {
        this.callback = callback;

        try {
            JSONObject objJson = new JSONObject();
            objJson.put("id", id);


            WebApiHelper myHelper = new WebApiHelper(new TaskCallBack() {
                @Override
                public void onSuccess(Object response) throws JSONException {
                    super.onSuccess(response);
                    try {
                        JSONObject apiResponse = new JSONObject(response.toString());
                        UserModel obj = new UserModel(apiResponse);
                        callback.onSuccess(obj);
                    } catch (JSONException e) {
                        Log.d("VS", e.toString());
                    }
                }
                public void onError(Object response)
                {
                    super.onError(response);
                    callback.onError(response);
                }
            });

            myHelper.execute(ConfigHelper.DataServiceUrl + "user/" + Integer.toString(id), "GET", objJson);
        } catch (Exception e) {
            Log.d("VS", e.toString());
        }
    }

    public void loadasync_email(final AsyncTaskCallback callback) {
        this.callback = callback;

        try {
            JSONObject objJson = new JSONObject();
            WebApiHelper myHelper = new WebApiHelper(new TaskCallBack() {
                @Override
                public void onSuccess(Object response) throws JSONException {
                    super.onSuccess(response);
                    try {
                        JSONObject apiResponse = new JSONObject(response.toString());
                        UserModel obj = new UserModel(apiResponse);
                        callback.onSuccess(obj);
                    } catch (JSONException e) {
                        Log.d("VS", e.toString());
                    }
                }

                public void onError(Object response) {
                    super.onError(response);
                    callback.onError(response);
                }
            });

            myHelper.execute(ConfigHelper.DataServiceUrl + "user/" +EmailAddress, "GET", objJson);
        } catch (Exception e)
        {
            Log.d("VS", e.toString());
        }

    }

    public void update(final AsyncTaskCallback callback) {  //for updating the data if email already exists
        this.callback = callback;

        try {
            JSONObject objJson = new JSONObject();
            objJson.put("profilePic",this.ProfilePic);
            objJson.put("firstName", this.FirstName);
            objJson.put("lastName", this.LastName);
            objJson.put("email", this.EmailAddress);
            objJson.put("gender", this.Gender);

            WebApiHelper myHelper = new WebApiHelper(new TaskCallBack() {
                @Override
                public void onSuccess(Object response) throws JSONException {
                    super.onSuccess(response);

                    JSONObject apiResponse = new JSONObject(response.toString());
                    try {
                        UserModel obj = new UserModel(apiResponse);
                        callback.onSuccess(obj);

                    } catch (JSONException e) {
                        Log.d("VS","UserModel"+ e.toString());
                    }
                }

                public void onError(Object response)
                {
                    super.onError(response);
                    callback.onError(response.toString());
                }
            });

            myHelper.execute(ConfigHelper.DataServiceUrl + "user/"+ID, "PUT", objJson);
        } catch (Exception e) {
            Log.d("VS", e.toString());
        }

    }

    // Save Patient
    public void saveDocAsync(final AsyncTaskCallback callback) {

        this.callback = callback;

        try {
            JSONObject objJson = new JSONObject();

            WebApiHelper myHelper = new WebApiHelper(new TaskCallBack() {
                @Override
                public void onSuccess(Object response) throws JSONException {
                    super.onSuccess(response);

                    JSONObject apiResponse = new JSONObject(response.toString());
                    try {
                        UserModel obj = new UserModel(apiResponse);
                        Log.d("VS",""+obj.ID);
                        callback.onSuccess(obj);
                    }
                    catch (JSONException e) {
                        Log.d("VS","UserModel"+ e.toString());
                    }
                }

                public void onError(Object response)
                {
                    super.onError(response);
                    callback.onError(response.toString());
                }
            });

            myHelper.execute(ConfigHelper.DataServiceUrl + "user/", "POST", objJson);
        } catch (Exception e)
        {
            Log.d("VS", e.toString());
        }
    }

}
