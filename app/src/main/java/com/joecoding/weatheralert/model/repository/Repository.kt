package com.joecoding.weatheralert.model.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData

import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.WeatherClient
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.LocalSourceDB
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.CurrentWeatherModel
import com.joecoding.weatheralert.providers.SharedPreferencesProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Repository(private val application: Application) {
    val LocalDB = LocalSourceDB.getInstance(application)
     var sharedPref: SharedPreferencesProvider=SharedPreferencesProvider(application)
    val latLong = sharedPref.latLong
    val latPref = latLong[0]
    val lngPref = latLong[1]



    fun fetchData(): LiveData<CurrentWeatherModel>{
        CoroutineScope(Dispatchers.IO).launch {
            val response= WeatherClient.getWeatherService()
                .getCurrentWeather(latPref,lngPref,"minutely","metric","en")
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    LocalDB.insert(response.body())
                    Log.d("responseeeeeeee", response.body().toString())
                }else{
                    Log.d("responseeeeeeee", "error")
                }
            }
        }
        return LocalDB.getAll()
    }



}