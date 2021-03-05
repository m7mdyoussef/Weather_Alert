package com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.CurrentWeatherModel

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoritePlaces(favoriteModel: FavoriteModel?)

    @Query("SELECT * From FavoriteModel")
    fun getFavoritePlaces(): LiveData<List<FavoriteModel>>


//
//    @Query("SELECT * From CurrentWeatherModel")
//    fun getAll(): LiveData<CurrentWeatherModel>?

    @Query("Delete from FavoriteModel WHERE lat=:lat AND lng=:lng  ")
   suspend fun deleteAll(lat:String , lng:String)
}