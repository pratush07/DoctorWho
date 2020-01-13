package com.example.pratush.docfinder.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pratush.docfinder.R;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BookAppointmentActivity extends ActionBarActivity {

    String date, time;
    int year, month, day, hour;
    int c_year, c_month, c_day, c_hour;
    DatePicker d;
    TimePicker t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);


    }

    public void pickdate(View view)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        final View promptView = layoutInflater.inflate(R.layout.date_picker_layout, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                d = (DatePicker) promptView.findViewById(R.id.datePicker);
                year = GregorianCalendar.getInstance().get(GregorianCalendar.YEAR);
                month = GregorianCalendar.getInstance().get(GregorianCalendar.MONTH);
                day = GregorianCalendar.getInstance().get(GregorianCalendar.DAY_OF_MONTH);
                //  Log.d("VS", year + " " + day + " " + month);
                //   Log.d("VS", d.getYear() + "/" + d.getMonth() + "/" + d.getDayOfMonth() );
                date = d.getYear() + "-" + (d.getMonth() + 1) + "-" + d.getDayOfMonth();
                TextView t = (TextView) findViewById(R.id.dateval);
                t.setText(date);
                c_year = d.getYear();
                c_month = d.getMonth();
                c_day = d.getDayOfMonth();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void picktime(View view)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        final View promptView = layoutInflater.inflate(R.layout.time_picker_layout, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                t = (TimePicker) promptView.findViewById(R.id.timePicker);
                hour = GregorianCalendar.getInstance().get(GregorianCalendar.HOUR);

                time = t.getCurrentHour() + ":" + ((t.getCurrentMinute() <= 9) ? "0" + t.getCurrentMinute() : t.getCurrentMinute());
                TextView td = (TextView) findViewById(R.id.timeval);
                td.setText(time);
                c_hour = t.getCurrentHour();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void bookappt(View view)
    {
        TextView t1 = (TextView) findViewById(R.id.dateval);
        TextView t2 = (TextView) findViewById(R.id.timeval);

        String str1=t1.getText().toString();
        String str2=t2.getText().toString();
        if(!str1.matches("") && !str2.matches(""))
        {
            if (year <= c_year && day <= c_day && month <= c_month) {//if old date is entered
                if (year == c_year && day == c_day && month == c_month) {
                    if (hour < c_hour) {
                        Intent i = new Intent(this, ThankyouActivity.class);
                        i.putExtra("date", date);
                        i.putExtra("time", time);
                        i.putExtra("doc_id", getIntent().getIntExtra("doc_id", 0));
                        startActivity(i);
                    } else
                        Toast.makeText(getApplicationContext(), "invalid time", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(this, ThankyouActivity.class);
                    i.putExtra("date", date);
                    i.putExtra("time", time);
                    i.putExtra("doc_id", getIntent().getIntExtra("doc_id", 0));
                    startActivity(i);
                }

            } else
                Toast.makeText(getApplicationContext(), "invalid date", Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(getApplicationContext(), "fill both the fields", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_appointment, menu);
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
