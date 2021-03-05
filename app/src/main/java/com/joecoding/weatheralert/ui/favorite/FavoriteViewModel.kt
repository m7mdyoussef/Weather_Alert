package com.joecoding.weatheralert.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.FavoriteModel
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.CurrentWeatherModel
import com.joecoding.weatheralert.model.repository.Repository
import kotlin.collections.List as List

class FavoriteViewModel (application: Application) : AndroidViewModel(application) {

   private val repository: Repository = Repository(application)

    private val navigate = MutableLiveData<List<String>>()


    fun getWeatherFromFavorite(lat2: String?, lng2: String?): LiveData<CurrentWeatherModel> {
        return  repository.fetchDataForFavorite(lat2,lng2)
    }

    fun insertFavoriteToDataBase(lat3: String?, lng3: String?){
        repository.fetchDataForFavorite(lat3,lng3)
    }


    //recyclerView Data handling
    fun insertFavorite(favoriteModel: FavoriteModel) {
       repository.insertFavoritePlaces(favoriteModel)
    }

    fun fetchFavorite() :LiveData<List<FavoriteModel>> {
        return repository.retriveFavoritePlaces()
    }

    fun deleteItem(lat:String , lng:String){
        repository.deleteFromDb(lat,lng)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////


    fun getNavigation(): MutableLiveData<List<String>> {
        return navigate
    }

    fun onClick(lat: String, lng: String) {
        val latLng = listOf<String>(lat,lng)
        navigate.value=latLng
    }




}