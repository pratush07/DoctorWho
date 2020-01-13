package com.example.pratush.docfinder.models;


import android.util.Log;

import com.example.pratush.docfinder.helpers.AsyncTaskCallback;
import com.example.pratush.docfinder.helpers.ConfigHelper;
import com.example.pratush.docfinder.helpers.TaskCallBack;
import com.example.pratush.docfinder.helpers.WebApiHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class DoctorAppTimeModel
{
    public int ID;
    public int doc_id;
    public int pat_id;
    public String appt_date;
    public String appt_time;
    public int accepted;

    AsyncTaskCallback callback;
    public DoctorAppTimeModel ()
    {

    }
    public DoctorAppTimeModel(JSONObject myData) throws JSONException
    {
        if (myData.has("doc_id")) this.doc_id = Integer.parseInt(myData.getString("doc_id"));
        if (myData.has("pat_id")) this.pat_id = Integer.parseInt(myData.getString("pat_id"));
        if (myData.has("appt_date")) this.appt_date = myData.getString("appt_date");
        if (myData.has("appt_time")) this.appt_time = myData.getString("appt_time");
        if (myData.has("id")) this.ID = Integer.parseInt(myData.getString("id"));
        if (myData.has("accepted")) this.accepted = Integer.parseInt(myData.getString("accepted"));

    }

    public void userAppointment(String type,final AsyncTaskCallback callback) {

        this.callback = callback;

        try {
            JSONObject objJson = new JSONObject();

            objJson.put("doc_id", this.doc_id);
            objJson.put("pat_id", this.pat_id);
            objJson.put("appt_date", this.appt_date);
            objJson.put("appt_time", this.appt_time);
            objJson.put("accepted", this.accepted);

            WebApiHelper myHelper = new WebApiHelper(new TaskCallBack() {
                @Override
                public void onSuccess(Object response) throws JSONException {
                    super.onSuccess(response);

                    callback.onSuccess(response);

                }

                public void onError(Object response)
                {
                    super.onError(response);
                    callback.onError(response.toString());
                }
            });

            if(type.equalsIgnoreCase("get"))
                myHelper.execute(ConfigHelper.DataServiceUrl + "bookappuser/"+pat_id, "GET", objJson);
            else if(type.equalsIgnoreCase("delete"))
                myHelper.execute(ConfigHelper.DataServiceUrl + "bookappuser/"+ID, "DELETE", objJson);//for deleting appointment
            else if(type.equalsIgnoreCase("post"))
                myHelper.execute(ConfigHelper.DataServiceUrl + "bookappuser/"+pat_id, "POST", objJson);

        } catch (Exception e)
        {
            Log.d("VS", e.toString());
        }
    }

    public void doctorAppointment(String type,final AsyncTaskCallback callback) {

        this.callback = callback;

        try {
            JSONObject objJson = new JSONObject();

            objJson.put("doc_id", this.doc_id);
            objJson.put("pat_id", this.pat_id);
            objJson.put("appt_date", this.appt_date);
            objJson.put("appt_time", this.appt_time);
            objJson.put("accepted", this.accepted);

            WebApiHelper myHelper = new WebApiHelper(new TaskCallBack() {
                @Override
                public void onSuccess(Object response) throws JSONException {
                    super.onSuccess(response);


                        callback.onSuccess(response);

                }

                public void onError(Object response)
                {
                    super.onError(response);
                    callback.onError(response.toString());
                }
            });

            if(type.equalsIgnoreCase("get"))
                myHelper.execute(ConfigHelper.DataServiceUrl + "bookappdoc/"+doc_id, "GET", objJson);
            else if(type.equalsIgnoreCase("put"))
                myHelper.execute(ConfigHelper.DataServiceUrl + "bookappdoc/"+ID, "PUT", objJson);   //for verifying appointment
            else if(type.equalsIgnoreCase("delete"))
                myHelper.execute(ConfigHelper.DataServiceUrl + "bookappdoc/"+ID, "DELETE", objJson);    //for deleting the appointment

        } catch (Exception e)
        {
            Log.d("VS", e.toString());
        }
    }

}
