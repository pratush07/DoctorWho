package com.example.pratush.docfinder.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.example.pratush.docfinder.R;
import com.example.pratush.docfinder.helpers.ConfigHelper;
import com.example.pratush.docfinder.helpers.TaskCallBack;
import com.example.pratush.docfinder.models.CategoryModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeScreenActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        Log.d("VS", "" + ConfigHelper.getUserID(getApplicationContext()));
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        EditText search = (EditText) findViewById(R.id.searchdoc);
        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                final ArrayList<String> cat_list= new ArrayList<String>();
                final ArrayList<Integer> catID_list= new ArrayList<Integer>();
                if (s.length() > 0) {
                    CategoryModel obj = new CategoryModel();
                    obj.cat = s.toString();

                    obj.searchAsync(new TaskCallBack() {
                        @Override
                        public void onSuccess(Object response) throws JSONException {
                            JSONArray res = new JSONArray(response.toString());
                            cat_list.clear();
                            catID_list.clear();
                            for (int i = 0; i < res.length(); i++) {
                                JSONObject resobj = (JSONObject) res.get(i);
                                cat_list.add(resobj.getString("cat"));
                                catID_list.add(resobj.getInt("catID"));
                            }
                            Log.d("VS", cat_list.toString());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ListView cat = (ListView) findViewById(R.id.categories);
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.cat_list_item, cat_list);
                                    cat.setAdapter(arrayAdapter);
                                    cat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        public void onItemClick(AdapterView parent, View v, int position, long id) {

                                            Intent i = new Intent(getApplicationContext(), DoctorsListActivity.class); //send catID ..get list of doctors in the next page
                                            i.putExtra("getcatID", catID_list.get(position));
                                            startActivity(i);
                                        }
                                    });

                                }
                            });

                        }

                        public void onError(Object response)
                        {

                        }
                    });
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });



    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        switch (position) {
            case 0:
                Intent intent = getIntent();    //refreshing
                finish();
                startActivity(intent);
                break;
            case 1:
                if (ConfigHelper.getType(this).equalsIgnoreCase("doc")) {
                    Intent i = new Intent(getApplicationContext(), DocAppointmentActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), UserAppointmentActivity.class);
                    startActivity(i);
                }

                break;


        }
    }




}
