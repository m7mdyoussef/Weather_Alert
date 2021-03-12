package com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response

import androidx.room.Embedded
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.joecoding.weatheralert.model.currentWeatherModel.db.converter.WeatherItemTypeConverter

@TypeConverters(WeatherItemTypeConverter::class)
data class DailyItem(

	@field:SerializedName("sunrise")
	val sunrise: Int? = null,

	@field:SerializedName("temp")
	@Embedded(prefix = "temp_")
	val temp: Temp? = null,

	@field:SerializedName("uvi")
	val uvi: Double? = null,

	@field:SerializedName("pressure")
	val pressure: Int? = null,

	@field:SerializedName("clouds")
	val clouds: Int? = null,

	@field:SerializedName("feels_like")
	@Embedded(prefix = "feelsLike_")
	val feelsLike: FeelsLike? = null,

	@field:SerializedName("dt")
	val dt: Int? = null,

	@field:SerializedName("pop")
	val pop: Double? = null,

	@field:SerializedName("wind_deg")
	val windDeg: Int? = null,

	@field:SerializedName("dew_point")
	val dewPoint: Double? = null,

	@field:SerializedName("snow")
	val snow: Double? = null,

	@field:SerializedName("sunset")
	val sunset: Int? = null,

	@field:SerializedName("weather")
	val weather: List<WeatherItem?>? = null,

	@field:SerializedName("humidity")
	val humidity: Int? = null,

	@field:SerializedName("wind_speed")
	val windSpeed: Double? = null,

	@field:SerializedName("rain")
	val rain: Double? = null
)