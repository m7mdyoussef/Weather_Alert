package com.joecoding.weatheralert.ui.favoriteDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.joecoding.weatheralert.R
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.CurrentWeatherModel
import com.joecoding.weatheralert.ui.favorite.FavoriteViewModel
import java.math.BigDecimal
import java.math.RoundingMode

class FavoriteDetailsActivity : AppCompatActivity() {

    private lateinit var favoriteDetailsViewModel: FavoriteDetailsViewModel

private var currentWeatherData: CurrentWeatherModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)

        val lat = intent.getStringExtra("lat")
        val lng = intent.getStringExtra("lng")

        Log.d("recieved", lat.toString())
        Log.d("recieved", lng.toString())

        favoriteDetailsViewModel = ViewModelProvider.AndroidViewModelFactory
            .getInstance(this.application)
            .create(FavoriteDetailsViewModel::class.java)

        //fetch favorite data from DB
        favoriteDetailsViewModel.getFavoriteWeatherData(lat,lng).observe(this, Observer {
        if(it != null) currentWeatherData=it

          Log.d("recieved", it.toString())

        })



    }
}