package com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.FavoriteDao
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.FavoriteModel
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.CurrentWeatherModel

@Database(entities = [CurrentWeatherModel::class,FavoriteModel::class], version = 3,exportSchema = false)
abstract class DataBase : RoomDatabase(){
    abstract fun weatherDao(): WeatherDao
    abstract fun favoriteDao(): FavoriteDao


}