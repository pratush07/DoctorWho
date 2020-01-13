package com.example.pratush.docfinder.activities;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pratush.docfinder.R;
import com.example.pratush.docfinder.helpers.ConfigHelper;
import com.example.pratush.docfinder.helpers.TaskCallBack;
import com.example.pratush.docfinder.models.DoctorAppTimeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ThankyouActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);

        DoctorAppTimeModel obj = new DoctorAppTimeModel();

        obj.doc_id = getIntent().getIntExtra("doc_id", 0);
        obj.pat_id = ConfigHelper.getUserID(this);
        obj.appt_date = getIntent().getStringExtra("date");
        obj.appt_time = getIntent().getStringExtra("time");
        obj.accepted = 0;

        obj.userAppointment("post", new TaskCallBack() {
            @Override
            public void onSuccess(Object response) throws JSONException {
                super.onSuccess(response);
                Handler mainHandler = new Handler(getApplicationContext().getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        ProgressBar p = (ProgressBar) findViewById(R.id.sendingBar);
                        TextView t = (TextView) findViewById(R.id.ty);

                        p.setVisibility(View.INVISIBLE);
                        t.setVisibility(View.VISIBLE);
                    }
                };
                mainHandler.post(myRunnable);

            }

            public void onError(Object response) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_thankyou, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
