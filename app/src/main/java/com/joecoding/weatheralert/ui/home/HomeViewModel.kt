package com.joecoding.weatheralert.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.joecoding.weatheralert.model.repository.Repository
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.weatherModel.CurrentWeatherModel
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application){


    var repository: Repository = Repository(application)

    fun updateUi(){

    }


    fun getWeather(): LiveData<CurrentWeatherModel> {

        return repository.fetchData()

    }

    fun refreshCurrentLocation() {
        viewModelScope.launch {
            repository.refreshCurrentLocation()
        }

    }






}