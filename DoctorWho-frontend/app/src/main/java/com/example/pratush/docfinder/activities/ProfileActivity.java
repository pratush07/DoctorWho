package com.example.pratush.docfinder.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pratush.docfinder.R;
import com.example.pratush.docfinder.helpers.ConfigHelper;
import com.example.pratush.docfinder.helpers.HTTPConnection;
import com.example.pratush.docfinder.helpers.PathJSONParser;
import com.example.pratush.docfinder.helpers.TaskCallBack;
import com.example.pratush.docfinder.models.CommentModel;
import com.example.pratush.docfinder.models.DoctorModel;
import com.example.pratush.docfinder.models.RateModel;
import com.example.pratush.docfinder.models.UserModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends ActionBarActivity{
    JSONObject profileDataObj;
    CommentModel Com;
    ArrayList<String> com = new ArrayList<>();
    GoogleMap map;
    LatLng origin,dest;
    ListView cat;
    MapView mapView;
    ViewGroup header,footer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        cat = (ListView) findViewById(R.id.commentlist);
        LayoutInflater inflater = getLayoutInflater();
        header = (ViewGroup)inflater.inflate(R.layout.profile_header_firstrow, cat, false);
        footer = (ViewGroup)inflater.inflate(R.layout.profile_footer_lastrowrow, cat, false);
        cat.addHeaderView(header, null, false);
        cat.addFooterView(footer, null, false);
        loadcomments();

       mapView = (MapView) header.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap Map) {
                map = Map;
                map.setMyLocationEnabled(true);
            }
        });

        final LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final Boolean network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(network_enabled==false)
            Toast.makeText(getApplicationContext(), "Location service is off,turn it on", Toast.LENGTH_LONG).show();
        else
            mLocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, mLocationListener, null);



    }

    public void loadcomments()
    {
        try {
            profileDataObj = new JSONObject(getIntent().getStringExtra("profiledata"));
            getSupportActionBar().setTitle(profileDataObj.getString("firstName")+" "+profileDataObj.getString("lastName"));
            DoctorModel obj = new DoctorModel();
            obj.loadAsync(profileDataObj.getInt("id"), new TaskCallBack() {
                @Override
                public void onSuccess(Object response) throws JSONException {
                    final DoctorModel obj = (DoctorModel) response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            TextView rate = (TextView) header.findViewById(R.id.Rate);
                            TextView qual = (TextView) header.findViewById(R.id.Qualvalue);
                            TextView con = (TextView) header.findViewById(R.id.consultval);
                            new DownloadImageTask((ImageView) findViewById(R.id.profilepic)).execute(obj.ProfilePic);
                            rate.setText("" + obj.Rate);
                            qual.setText(obj.Qualification);
                            con.setText("" + obj.Consultation);

                        }
                    });
                }


                public void onError(Object response)
                {

                }
            });


            Com=new CommentModel();
            Com.com_from = ConfigHelper.getUserID(getApplicationContext());
            Com.com_to = profileDataObj.getInt("id");

            Com.comment_retrieve(new TaskCallBack() {   //retrieving stored comments
                @Override
                public void onSuccess(Object response) throws JSONException {
                    JSONArray comments = new JSONArray(response.toString());
                    com.clear();
                    com.add("Comments");
                    UserModel user = new UserModel();
                    for (int i = 0; i < comments.length(); i++) {
                        final JSONObject t = (JSONObject) comments.get(i);

                        user.loadAsync(t.getInt("com_from"), new TaskCallBack() {   //retrieving stored comments from user table
                            @Override
                            public void onSuccess(Object response) throws JSONException {
                                UserModel user = (UserModel) response;
                                com.add(user.FirstName + " " + user.LastName + " : " + t.getString("com"));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.appointmentlist_layout, com);
                                        cat.setAdapter(arrayAdapter);
                                    }
                                });
                            }

                            public void onError(Object response) {

                            }
                        });

                    }

                    if (comments.length() == 0)    //if comment is empty
                    {
                        com.clear();
                        com.add("Comments");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.appointmentlist_layout, com);
                                cat.setAdapter(arrayAdapter);
                            }
                        });
                    }
                }


                public void onError(Object response)
                {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /* public class EfficientAdapter extends ArrayAdapter {

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

             return convertView;
         }

     }*/
    public void comment(View view) {
        EditText ed = (EditText) findViewById(R.id.input);
        if(Com.com_from!=Com.com_to && !ConfigHelper.getType(getApplicationContext()).equalsIgnoreCase("doc") && !ed.getText().toString().equalsIgnoreCase("")) {

            Com.com = ed.getText().toString();

            Com.comment_store(new TaskCallBack() { //storing comments
                @Override
                public void onSuccess(Object response) throws JSONException {
                    final RateModel rateobj = new RateModel();
                    rateobj.rate_to = profileDataObj.getInt("id");

                    rateobj.getAverageRate(new TaskCallBack() {   //retrieving stored comments
                        @Override
                        public void onSuccess(Object response) throws JSONException {
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }


                        public void onError(Object response) {

                        }
                    });


                }

                public void onError(Object response) {

                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),"Not Allowed",Toast.LENGTH_LONG).show();
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void rate (View view) throws JSONException {
        TextView rateval = (TextView) findViewById(R.id.ratevalue);
        if(Com.com_from!=Com.com_to && !ConfigHelper.getType(getApplicationContext()).equalsIgnoreCase("doc") && !rateval.getText().toString().equalsIgnoreCase("")) {

            final RateModel rateobj = new RateModel();
            rateobj.rate_to = profileDataObj.getInt("id");
            rateobj.rate_from = ConfigHelper.getUserID(getApplicationContext());
            rateobj.rate = Integer.parseInt(rateval.getText().toString());
            rateobj.saveRateAsync(new TaskCallBack() {
                @Override
                public void onSuccess(Object response) {

                    rateobj.getAverageRate(new TaskCallBack() {   //retrieving stored comments
                        @Override
                        public void onSuccess(Object response) throws JSONException {
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }


                        public void onError(Object response) {

                        }
                    });

                }

                public void onError(Object response) {

                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Not Allowed.",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //if patient wants to book
        if (id == R.id.book)
        {
            if(Com.com_from!=Com.com_to && !ConfigHelper.getType(getApplicationContext()).equalsIgnoreCase("doc"))
            {
                Intent i = new Intent(getApplicationContext(),BookAppointmentActivity.class);
                try {
                    i.putExtra("doc_id", profileDataObj.getInt("id"));
                } catch (JSONException e) {

                }
                startActivity(i);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Not Allowed.",Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }



    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {

            origin=new LatLng(location.getLatitude(), location.getLongitude()); //the origin
            Geocoder coder = new Geocoder(getApplicationContext());
            try {
                ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(profileDataObj.getString("address"), 50);
                for(Address add : adresses)
                {
                    dest=new LatLng(add.getLatitude(), add.getLongitude()); //getting dest
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            MarkerOptions options = new MarkerOptions();
            options.position(origin);
            options.position(dest);
            map.addMarker(options);
            String url = getMapsApiDirectionsUrl();
            ReadTask downloadTask = new ReadTask();
            downloadTask.execute(url);
            double midplat=(origin.latitude+dest.latitude)/2;
            double midplong=(origin.longitude+dest.longitude)/2;
            LatLng midp = new LatLng(midplat,midplong);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(midp,12));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }


    };

    private String getMapsApiDirectionsUrl() {
        String waypoints = "waypoints=optimize:true|" +origin.latitude + "," +origin.longitude + "|"
                + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        String orig_dest="origin="+origin.latitude+","+origin.longitude+"&destination="+dest.latitude+","+dest.longitude+"&%20";
        String url = "https://maps.googleapis.com/maps/api/directions/"+ output + "?" +orig_dest+ params;
        return url;
    }
    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HTTPConnection http = new HTTPConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(4);
                polyLineOptions.color(Color.BLUE);
            }

            map.addPolyline(polyLineOptions);
        }


    }



}
