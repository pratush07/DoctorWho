package com.example.pratush.docfinder.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pratush.docfinder.R;
import com.example.pratush.docfinder.helpers.ConfigHelper;
import com.example.pratush.docfinder.helpers.TaskCallBack;
import com.example.pratush.docfinder.models.DoclistModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class DoctorsListActivity extends ActionBarActivity {

    ArrayList<String> name_list= new ArrayList<String>();
    ArrayList<JSONObject> objs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list);

        DoclistModel obj = new DoclistModel();
        obj.catID=getIntent().getIntExtra("getcatID", 0);
        obj.usrID = ConfigHelper.getUserID(getApplicationContext());

        obj.getDocListAsync(new TaskCallBack() {
            @Override
            public void onSuccess(Object response) throws JSONException {
                JSONArray res = new JSONArray(response.toString());

                for (int i = 0; i < res.length(); i++) {
                    JSONObject resobj = (JSONObject) res.get(i);
                    objs.add(resobj);
                    name_list.add(resobj.getString("firstName") + " " + resobj.getString("lastName"));

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListView cat = (ListView) findViewById(R.id.doclistcat);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.appointmentlist_layout, name_list);
                        cat.setAdapter(arrayAdapter);
                        cat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView parent, View v, int position, long id) {


                                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                                i.putExtra("profiledata", objs.get(position).toString());
                                startActivity(i);
                            }
                        });

                    }
                });
            }
        });

    }







}
