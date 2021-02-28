package com.joecoding.weatheralert.providers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesProvider {

    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    private static final String PREF_NAME = "SHARED_PREFERENCE";

    // Shared preferences for Intro_Pager
    private static final String IS_FIRST_TIME_LAUNCH = "IS_FIRST_TIME_LAUNCH";

    // Shared preferences for LAT_LONG
    private static final String LAT_SHARED_PREF = "LAT_SHARED_PREF";
    private static final String LONG_SHARED_PREF = "LONG_SHARED_PREF";


    public SharedPreferencesProvider(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }


    /***********************************************************************************************
     */
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    /***********************************************************************************************
     */

    public void setLatLong(String latitude, String longitude) {

        editor.putString(LAT_SHARED_PREF, latitude);
        editor.putString(LONG_SHARED_PREF, longitude);
        editor.commit();
    }

    public String[] getLatLong() {
        String[] Location = new String[2];
        String lat=pref.getString(LAT_SHARED_PREF,"lat");
        String lng=pref.getString(LONG_SHARED_PREF,"long");
        Location[0]=lat;
        Location[1]=lng;
        return Location;
    }
}
