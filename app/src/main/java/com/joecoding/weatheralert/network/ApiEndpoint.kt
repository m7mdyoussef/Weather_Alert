package com.joecoding.weatheralert.network



object ApiEndpoint {
    var BASEURL = "http://api.openweathermap.org/data/2.5/"
    var CurrentWeather = "weather?"
    var ListWeather = "forecast?"
    var Daily = "forecast/daily?"
    var UnitsAppid = "&units=metric&appid=d777ae60141a13cd6e1dd200ac6c5fdb"
    var UnitsAppidDaily = "&units=metric&cnt=15&appid=d777ae60141a13cd6e1dd200ac6c5fdb"
}
