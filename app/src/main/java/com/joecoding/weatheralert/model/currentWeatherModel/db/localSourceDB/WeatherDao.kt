package com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.CurrentWeatherModel

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentWeatherModel: CurrentWeatherModel?)

    @Query("SELECT * From CurrentWeatherModel WHERE lat=:lat AND lon=:lon")
    fun getAll(lat: String?, lon: String?): LiveData<CurrentWeatherModel>

    @Query("Delete from CurrentWeatherModel WHERE lat=:lat AND lon=:lng  ")
    suspend fun deleteAllWeather(lat:String , lng:String)
//
//    @Query("SELECT * From CurrentWeatherModel")
//    fun getAll(): LiveData<CurrentWeatherModel>?

//    @Query("Delete from WeatherMainModel")
//    fun deleteAll()
}