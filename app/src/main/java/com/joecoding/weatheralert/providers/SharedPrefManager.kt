package com.joecoding.weatheralert.providers

import android.content.Context
import android.content.SharedPreferences



private const val PREF_NAME = "SHARED_PREFERENCE"

// Shared preferences for Intro_Pager
private const val IS_FIRST_TIME_LAUNCH = "IS_FIRST_TIME_LAUNCH"

// Shared preferences for LAT_LONG
private const val LAT_SHARED_PREF = "LAT_SHARED_PREF"
private const val LONG_SHARED_PREF = "LONG_SHARED_PREF"

class SharedPrefManager(context: Context){

    private val pref: SharedPreferences? = null
    private val editor: SharedPreferences.Editor? = null

   // pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)


}