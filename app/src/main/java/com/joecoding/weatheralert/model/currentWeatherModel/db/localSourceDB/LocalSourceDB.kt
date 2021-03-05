package com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB

import android.app.Application
import androidx.room.Room

object LocalSourceDB {
    fun getInstance(application: Application): DataBase {
        return Room.databaseBuilder(application,
            DataBase::class.java,
            "WeatherDataBase")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    }
}