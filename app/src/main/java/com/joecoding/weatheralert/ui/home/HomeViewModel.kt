package com.joecoding.weatheralert.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.joecoding.weatheralert.model.repository.Repository
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.weatherModel.CurrentWeatherModel

class HomeViewModel(application: Application) : AndroidViewModel(application){

   lateinit var currentWeatherLiveData :LiveData<CurrentWeatherModel>
    var repository: Repository = Repository(application)


    fun getWeather(): LiveData<CurrentWeatherModel> {
        currentWeatherLiveData= repository.fetchData()
        return currentWeatherLiveData
    }




}