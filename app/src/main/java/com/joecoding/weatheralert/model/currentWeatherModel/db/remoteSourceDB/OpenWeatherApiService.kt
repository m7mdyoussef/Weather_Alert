package com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB

import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.CurrentWeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY="d777ae60141a13cd6e1dd200ac6c5fdb"

//(ApiEndpoint.BASEURL + ApiEndpoint.CurrentWeather + "lat=" + lat + "&lon=" + lng + ApiEndpoint.UnitsAppid)

/*
*   var BASEURL = "http://api.openweathermap.org/data/2.5/"
    var CurrentWeather = "weather?"
    var ListWeather = "forecast?"
    var Daily = "forecast/daily?"
    var UnitsAppid = "&units=metric&appid=d777ae60141a13cd6e1dd200ac6c5fdb"
    var UnitsAppidDaily = "&units=metric&cnt=15&appid=d777ae60141a13cd6e1dd200ac6c5fdb"
* */

// https://api.openweathermap.org/data/2.5/onecall?lat=55.755825&lon=37.617298
// &units=metric&exclude=minutely&appid=d777ae60141a13cd6e1dd200ac6c5fdb
interface OpenWeatherApiService {

    @GET("onecall")
    suspend fun getCurrentWeather(@Query("lat") lat:String,
                                  @Query("lon") lon:String,
                                  @Query("exclude") exclude:String,
                                  @Query("units") units:String ,
                                  @Query("lang") lang: String,
                                  @Query("appid") appid: String= API_KEY
    ): Response<CurrentWeatherModel>

}