package com.joecoding.weatheralert.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.weatherModel.CurrentWeatherModel
import com.joecoding.weatheralert.model.repository.Repository

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    var repository: Repository = Repository(application)


    fun getWeather(): LiveData<CurrentWeatherModel> {

        return repository.fetchData()

    }
}