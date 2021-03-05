package com.joecoding.weatheralert.model.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData

import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.WeatherClient
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.LocalSourceDB
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.FavoriteModel
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.CurrentWeatherModel
import com.joecoding.weatheralert.providers.SharedPreferencesProvider
import kotlinx.coroutines.*

class Repository(private val application: Application) {
    val localWeatherDB = LocalSourceDB.getInstance(application).weatherDao()
    val localFavoriteDB = LocalSourceDB.getInstance(application).favoriteDao()

    var sharedPref: SharedPreferencesProvider = SharedPreferencesProvider(application)
    val latLong = sharedPref.latLong

    val latPref = latLong[0]
    val lngPref = latLong[1]

    private val imperialUnit = sharedPref.isUnitsConvertedToImperial




    fun fetchData(): LiveData<CurrentWeatherModel> {
        CoroutineScope(Dispatchers.IO).launch{
                try {
                    val units = if(imperialUnit){ "imperial" }else{ "metric" }
                    val response = WeatherClient.getWeatherService()
                        .getCurrentWeather(latPref, lngPref, "minutely", units, "en")
                    Log.d("responseeeeeeee", latPref+"--------"+lngPref)

                    if (response.isSuccessful) {
                        localWeatherDB.insert(response.body())
                        Log.d("responseeeeeeee", response.body().toString())
                    } else {
                        Log.d("responseeeeeeee", response.message())
                    }
                }catch (e:Exception){
                    Log.d("responseeeeeeee", e.message.toString())
                }
            }

        Log.d("responseeeeeeee", "ffffffffffffffffffffff")
          return localWeatherDB.getAll(latPref,lngPref)
    }


    fun fetchDataForFavorite(favLat: String?, favLng: String?): LiveData<CurrentWeatherModel> {

        CoroutineScope(Dispatchers.IO).launch{
                try {
                    val response = WeatherClient.getWeatherService()
                        .getCurrentWeather(favLat, favLng, "minutely", "metric", "en")
                    Log.d("responseeeeeeee", favLat+"--------"+favLng)
                    if (response.isSuccessful) {
                        localWeatherDB.insert(response.body())
                        Log.d("responseeeeeeee", response.body().toString())
                    } else {
                        Log.d("responseeeeeeee", response.message())
                    }
                }catch (e:Exception){
                    Log.d("responseeeeeeee", e.message.toString())
                }
            }

        Log.d("responseeeeeeee", "ffffffffffffffffffffff")
        return localWeatherDB.getAll(favLat,favLng)
    }


    fun insertFavoritePlaces(favoriteModel: FavoriteModel){
     localFavoriteDB.insertFavoritePlaces(favoriteModel)
    }


    fun retriveFavoritePlaces(): LiveData<List<FavoriteModel>>{
        return  localFavoriteDB.getFavoritePlaces()
    }



    fun deleteFromDb(lat:String , lng: String){
        CoroutineScope(Dispatchers.IO).launch {
            localFavoriteDB.deleteAll(lat,lng)
            localWeatherDB.deleteAllWeather(lat,lng)
        }

    }





}



