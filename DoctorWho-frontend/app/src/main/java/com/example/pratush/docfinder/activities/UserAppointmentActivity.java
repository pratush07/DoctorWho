package com.example.pratush.docfinder.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pratush.docfinder.R;
import com.example.pratush.docfinder.helpers.ConfigHelper;
import com.example.pratush.docfinder.helpers.TaskCallBack;
import com.example.pratush.docfinder.models.DoctorAppTimeModel;
import com.example.pratush.docfinder.models.DoctorModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class UserAppointmentActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private NavigationDrawerFragment mNavigationDrawerFragment;
    DoctorAppTimeModel obj;
    ArrayList<String> listdocname = new ArrayList<>();
    ArrayList<Integer> positionidlink = new ArrayList<>();
    ArrayList<Integer> positionacceptedlink = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_appointment);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        obj = new DoctorAppTimeModel();
        obj.pat_id = ConfigHelper.getUserID(getApplicationContext());

        obj.userAppointment("get", new TaskCallBack() {
            @Override
            public void onSuccess(Object response) throws JSONException {
                super.onSuccess(response);
                JSONArray appts = new JSONArray(response.toString());

                for (int i = 0; i < appts.length(); i++) {
                    final JSONObject t = (JSONObject) appts.get(i);

                    final DoctorAppTimeModel apptobj = new DoctorAppTimeModel(t);
                    positionidlink.add(apptobj.ID);
                    positionacceptedlink.add(apptobj.accepted);
                    DoctorModel doc = new DoctorModel();
                    doc.loadAsync(apptobj.doc_id, new TaskCallBack() { //getting the doc names
                        @Override
                        public void onSuccess(Object response) throws JSONException {
                            DoctorModel obj = (DoctorModel) response;
                            listdocname.add(obj.FirstName + " " + obj.LastName + "      " + apptobj.appt_date + " " + apptobj.appt_time + "  " + ((apptobj.accepted == 0) ? "pending" : "accepted"));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ListView cat = (ListView) findViewById(R.id.appointmentlistuser);
                                    // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.appointmentlist_layout, listdocname);
                                    cat.setAdapter(new EfficientAdapter(getApplicationContext(), R.layout.appointmentlist_layout, listdocname));
                                    cat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        public void onItemClick(AdapterView parent, View v, final int position, long id) {
                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserAppointmentActivity.this);
                                            // Setting Dialog Title
                                            alertDialog.setTitle("Change Appointment");

                                            // Setting Dialog Message
                                            alertDialog.setMessage("To delete the appointment press delete");
                                            alertDialog.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    DoctorAppTimeModel delobj = new DoctorAppTimeModel();
                                                    delobj.ID = positionidlink.get(position);
                                                    delobj.userAppointment("delete", new TaskCallBack() {   //deleting specific appointment from user side
                                                        @Override
                                                        public void onSuccess(Object response) throws JSONException {
                                                            Intent intent = getIntent();    //refreshing
                                                            finish();
                                                            startActivity(intent);
                                                        }

                                                        public void onError(Object response) {

                                                        }
                                                    });


                                                }
                                            });
                                            alertDialog.show();
                                        }
                                    });
                                }
                            });
                        }

                        public void onError(Object response) {

                        }
                    });
                }


            }

            public void onError(Object response) {

            }
        });
    }

    public class EfficientAdapter extends ArrayAdapter {

        private LayoutInflater mInflater;

        private ArrayList<String> mStrings;

        private int mViewResourceId;

        public EfficientAdapter(Context ctx, int viewResourceId, ArrayList<String> listdocname) {
            super(ctx, viewResourceId, listdocname);

            mInflater = (LayoutInflater) ctx.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            mStrings = listdocname;

            mViewResourceId = viewResourceId;
        }

        @Override
        public int getCount() {
            return mStrings.size();
        }

        @Override
        public String getItem(int position) {
            return mStrings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(mViewResourceId, null);
            convertView.setMinimumHeight(132);
            TextView tv = (TextView) convertView.findViewById(R.id.listitem); //Give Id to your textview
            tv.setText(mStrings.get(position));
            if (positionacceptedlink.get(position) == 0)
                tv.setTextColor(Color.RED);
            else
                tv.setTextColor(Color.GREEN);
            return convertView;
        }

    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        switch (position) {
            case 0:
                Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                startActivity(i);

                break;
            case 1:
                Intent intent = getIntent();    //refreshing
                finish();
                startActivity(intent);
                break;

        }
    }

}
