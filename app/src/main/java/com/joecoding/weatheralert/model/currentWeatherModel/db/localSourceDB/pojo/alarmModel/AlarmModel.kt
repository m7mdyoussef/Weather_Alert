package com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.alarmModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.joecoding.weatheralert.model.currentWeatherModel.db.converter.AlarmItemTypeConverter
import com.joecoding.weatheralert.model.currentWeatherModel.db.converter.WeatherItemTypeConverter

@Entity
@JvmSuppressWildcards
@TypeConverters(AlarmItemTypeConverter::class)
data class AlarmModel (
    @PrimaryKey(autoGenerate = true) var id: Int=0,
    val weatherState: String,
    val accurateState: String,
    val daily: List<MaterialDayPicker.Weekday>,
    val billingTime: String,
    val userDescription: String,
    val minMaxChoice: String,
    val value:Double
){

}





