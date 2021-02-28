package com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.joecoding.weatheralert.model.currentWeatherModel.db.converter.AlertItemTypeConverter
import com.joecoding.weatheralert.model.currentWeatherModel.db.converter.DailyItemTypeConverter
import com.joecoding.weatheralert.model.currentWeatherModel.db.converter.HourlyItemTypeConverter

@Entity(primaryKeys = ["lon","lat"])
@JvmSuppressWildcards
@TypeConverters(AlertItemTypeConverter::class,
	            DailyItemTypeConverter::class,
	            HourlyItemTypeConverter::class)

data class CurrentWeatherModel(

	@field:SerializedName("alerts")
	val alerts: List<AlertsItem?>? = null,

	@field:SerializedName("current")
	@Embedded(prefix = "current_")
	val current: Current? = null,

	@field:SerializedName("timezone")
	val timezone: String? = null,

	@field:SerializedName("timezone_offset")
	val timezoneOffset: Int? = null,

	@field:SerializedName("daily")
	val daily: List<DailyItem?>? = null,

	@field:SerializedName("lon")
	val lon: Double,

	@field:SerializedName("hourly")
	val hourly: List<HourlyItem?>? = null,

	@field:SerializedName("lat")
	val lat: Double
)