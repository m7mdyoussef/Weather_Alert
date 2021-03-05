package com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo

import androidx.room.Entity

@Entity(primaryKeys = ["lat","lng"])
data class FavoriteModel (
    var place: String? = null,
    var lat: String,
    var lng: String
)





