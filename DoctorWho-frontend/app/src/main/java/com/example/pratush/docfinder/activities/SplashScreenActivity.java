package com.example.pratush.docfinder.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.pratush.docfinder.R;
import com.example.pratush.docfinder.helpers.ConfigHelper;
import com.example.pratush.docfinder.helpers.TaskCallBack;
import com.example.pratush.docfinder.models.DoctorModel;
import com.example.pratush.docfinder.models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;


public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        if (ConfigHelper.getUserID(getApplicationContext()) > 0)  //if user is  registered
        {

            //check for id in database
            if(ConfigHelper.internetAvailable(getApplicationContext())==1) {

                if (ConfigHelper.getType(getApplicationContext()).equalsIgnoreCase("usr"))//if user/patient
                {
                    UserModel use = new UserModel();
                    use.loadAsync(ConfigHelper.getUserID(getApplicationContext()), new TaskCallBack() {
                        @Override
                        public void onSuccess(Object response) throws JSONException //if id exists
                        {
                            super.onSuccess(response);
                            //go to home screen
                            Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }

                        public void onError(Object response)   //if id doesn't exist in database
                        {
                            super.onError(response);
                            String errorresp = response.toString();
                            if (errorresp == "ConnectionError") {
                                Intent i = new Intent(getApplicationContext(), NoInternetActivity.class); //show error page
                                startActivity(i);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            } else {
                                Intent i = new Intent(getApplicationContext(), UserTypeActivity.class); //proceed to login
                                startActivity(i);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        }
                    });
                }
                else    //if doctor
                {
                    DoctorModel doc= new DoctorModel();
                    doc.loadAsync(ConfigHelper.getUserID(getApplicationContext()), new TaskCallBack() {
                        @Override
                        public void onSuccess(Object response) throws JSONException //if id exists
                        {
                            super.onSuccess(response);
                            //go to home screen
                            Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }

                        public void onError(Object response)   //if id doesn't exist in database
                        {
                            super.onError(response);
                            String errorresp = response.toString();
                            if (errorresp == "ConnectionError") {
                                Intent i = new Intent(getApplicationContext(), NoInternetActivity.class); //show error page
                                startActivity(i);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            } else {
                                Intent i = new Intent(getApplicationContext(), UserTypeActivity.class); //proceed to login
                                startActivity(i);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        }
                    });
                }
            }

                else {
                Toast.makeText(getApplicationContext(), "Internet Unavailable!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), NoInternetActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }

        } else {
            //proceed to login
            Intent i = new Intent(getApplicationContext(), UserTypeActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

    }

}
