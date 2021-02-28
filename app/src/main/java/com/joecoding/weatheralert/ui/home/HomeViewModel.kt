package com.joecoding.weatheralert.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.joecoding.weatheralert.model.repository.Repository
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.CurrentWeatherModel

class HomeViewModel(application: Application) : AndroidViewModel(application){

   // val CurrentWeatherLiveData = MutableLiveData<CurrentWeatherModel>()

    val repository = Repository(application)

    fun getWeather(): LiveData<CurrentWeatherModel> {
        return repository.fetchData()
    }


}