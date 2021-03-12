package com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.alarmModel.AlarmDao
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.alarmModel.AlarmModel
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.favoritePlacesModel.FavoriteDao
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.favoritePlacesModel.FavoriteModel
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.weatherModel.WeatherDao
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.weatherModel.CurrentWeatherModel

@Database(entities = [CurrentWeatherModel::class, FavoriteModel::class,AlarmModel::class], version =1 ,exportSchema = false)
abstract class DataBase : RoomDatabase(){
    abstract fun weatherDao(): WeatherDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun alarmDao(): AlarmDao


}