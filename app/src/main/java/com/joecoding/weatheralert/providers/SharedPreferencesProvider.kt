package com.joecoding.weatheralert.providers

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesProvider(context: Context) {


    companion object {
        private lateinit var pref: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor
        private const val PREF_NAME = "SHARED_PREFERENCE"

        // Shared preferences for Intro_Pager
        private const val IS_FIRST_TIME_LAUNCH = "IS_FIRST_TIME_LAUNCH"

        // Shared preferences for location
        private const val IS_LOCATION_ENABLED = "IS_LOCATION_ENABLED"

        // Shared preferences for internet
        private const val IS_THE_INTERNET_ENABLED = "IS_THE_INTERNET_ENABLED"

        // Shared preferences for LAT_LONG
        private const val LAT_SHARED_PREF = "LAT_SHARED_PREF"
        private const val LONG_SHARED_PREF = "LONG_SHARED_PREF"

        // shared preference for units and language
        private const val IMPERIAL_UNITS_SHARED_PREF = "IMPERIAL_UNITS_SHARED_PREF"
        private const val METRIC_UNITS_SHARED_PREF = "METRIC_UNITS_SHARED_PREF"

        private const val LANGUAGE_SHARED_PREF = "LANGUAGE_SHARED_PREF"

    }

    init {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    /***********************************************************************************************
     */
    fun setFirstTimeLaunch(isFirstTime: Boolean){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
        editor.commit()
    }

    val isFirstTimeLaunch: Boolean
    get()= pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)

    /***********************************************************************************************
     */

    fun convertUnitsToImperial(converted: Boolean){
        editor.putBoolean(IMPERIAL_UNITS_SHARED_PREF, converted)
        editor.commit()
    }

    val isUnitsConvertedToImperial: Boolean
        get()= pref.getBoolean(IMPERIAL_UNITS_SHARED_PREF, false)

    /***********************************************************************************************
     */

    fun convertUnitsToMetric(converted: Boolean){
        editor.putBoolean(METRIC_UNITS_SHARED_PREF, converted)
        editor.commit()
    }

    val isUnitsConvertedToMetric: Boolean
        get()= pref.getBoolean(METRIC_UNITS_SHARED_PREF, false)

    /***********************************************************************************************
     */
    fun setFirstTimeLocationenabled(isFirstTime: Boolean) {
        editor.putBoolean(IS_LOCATION_ENABLED, isFirstTime)
        editor.commit()
    }

    val isFirstTimeLocationEnabled: Boolean
        get() = pref.getBoolean(IS_LOCATION_ENABLED, false)

    /***********************************************************************************************
     */
    fun setLatLong(latitude: String?, longitude: String?) {
        editor.putString(LAT_SHARED_PREF, latitude)
        editor.putString(LONG_SHARED_PREF, longitude)
        editor.commit()
    }

    val latLong: Array<String?>
        get() {
            val location = arrayOfNulls<String>(2)
            val lat = pref.getString(LAT_SHARED_PREF, "0.0")
            val lng = pref.getString(LONG_SHARED_PREF, "0.0")
            location[0] = lat
            location[1] = lng
            return location
        }


}