package com.example.pratush.docfinder.helpers;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConfigHelper
{
    //dataservice url
    public static String DataServiceUrl = "http://192.168.43.59:8000/doc/";
    // Shared Pref Name
    private static String SharedPrefName = "UserPref";

    public static int internetAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo!=null && activeNetworkInfo.isConnected())
            return 1;
        else
            return 0;
    }
    public static int getUserID(Context myContext) {
        SharedPreferences mySharedPref = myContext.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        int PatientID = mySharedPref.getInt("UserID", 0);
        return PatientID;
    }

    public static void setUserID( int myPatientID, Context myContext) {
        SharedPreferences mySharedPref = myContext.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = mySharedPref.edit();
        myEditor.putInt("UserID", myPatientID);
        myEditor.commit();
    }

    public static String getType(Context myContext) {
        SharedPreferences mySharedPref = myContext.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        String Type = mySharedPref.getString("Type","usr");
        return Type;
    }

    public static void setType( String Type, Context myContext) {
        SharedPreferences mySharedPref = myContext.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = mySharedPref.edit();
        myEditor.putString("Type", Type);
        myEditor.commit();
    }

}
