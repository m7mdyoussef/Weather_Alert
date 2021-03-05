package com.joecoding.weatheralert.ui.favoriteDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.CurrentWeatherModel
import com.joecoding.weatheralert.model.repository.Repository

class FavoriteDetailsViewModel (application: Application) : AndroidViewModel(application) {
    private val repository: Repository = Repository(application)

    fun getFavoriteWeatherData(lat2: String?, lng2: String?): LiveData<CurrentWeatherModel> {
        return  repository.fetchDataForFavorite(lat2,lng2)
    }

}