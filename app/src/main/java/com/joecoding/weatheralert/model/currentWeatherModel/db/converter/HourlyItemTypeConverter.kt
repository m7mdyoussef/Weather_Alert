package com.joecoding.weatheralert.model.currentWeatherModel.db.converter

import androidx.room.TypeConverter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.HourlyItem

class HourlyItemTypeConverter {
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromHourlyItemList(value: MutableList<HourlyItem>): String {
            val gson = Gson()
            val type = object : TypeToken<MutableList<HourlyItem>>() {}.type
            return gson.toJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun toHourlyItemList(value: String): MutableList<HourlyItem> {
            val gson = Gson()
            val type = object : TypeToken<MutableList<HourlyItem>>() {}.type
            return gson.fromJson(value, type)
        }
    }
}

