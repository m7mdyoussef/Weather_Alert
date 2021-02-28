package com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.CurrentWeatherModel

@Database(entities = [CurrentWeatherModel::class], version = 2,exportSchema = false)
abstract class DataBase : RoomDatabase(){
    abstract fun weatherDao(): WeatherDao
}