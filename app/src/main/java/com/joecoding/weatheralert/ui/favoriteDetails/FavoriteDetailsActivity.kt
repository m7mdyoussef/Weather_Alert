package com.joecoding.weatheralert.ui.favoriteDetails

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.joecoding.weatheralert.R
import com.joecoding.weatheralert.adapter.MainAdapter
import com.joecoding.weatheralert.databinding.ActivityFavoriteDetailsBinding
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.weatherModel.CurrentWeatherModel
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.HourlyItem
import com.joecoding.weatheralert.network.ApiUnits
import com.joecoding.weatheralert.providers.MyContextWrapper
import com.joecoding.weatheralert.providers.SharedPreferencesProvider
import java.io.IOException

import java.text.SimpleDateFormat
import java.util.*


class FavoriteDetailsActivity : AppCompatActivity() {

    private var mainAdapter: MainAdapter? = null

    lateinit var binding: ActivityFavoriteDetailsBinding

    private var fabNextDaysFav : ExtendedFloatingActionButton? = null
    private lateinit var favoriteDetailsViewModel: FavoriteDetailsViewModel
    lateinit var sharedPref: SharedPreferencesProvider
    private lateinit var lang: String
    var address: String = ""
    override fun attachBaseContext(newBase: Context?) {
        sharedPref = SharedPreferencesProvider(newBase!!)
        lang = sharedPref.getLanguage!!

        super.attachBaseContext(MyContextWrapper.wrap(newBase,lang))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fabNextDaysFav = findViewById(R.id.fabNextDaysFav)


        val lat = intent.getStringExtra("lat")
        val lng = intent.getStringExtra("lng")



        val fragmentNextDaysFav = FragmentNextDaysFav.newInstance("FragmentNextDays")
        fabNextDaysFav?.setOnClickListener {

            fragmentNextDaysFav.show(supportFragmentManager, fragmentNextDaysFav.tag)
        }
        Log.d("recieved", lat.toString())
        Log.d("recieved", lng.toString())

        favoriteDetailsViewModel = ViewModelProvider.AndroidViewModelFactory
            .getInstance(this.application)
            .create(FavoriteDetailsViewModel::class.java)

        //fetch favorite data from DB
        favoriteDetailsViewModel.getFavoriteWeatherData(lat,lng).observe(this, Observer {
            if(it != null) {
                val hourly: List<HourlyItem?>? = it.hourly
                mainAdapter = MainAdapter(this,hourly)


                Log.d("dataaaaaaa", it.toString())

                binding.rvListWeatherHome.adapter = mainAdapter
                binding.rvListWeatherHome.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                binding.rvListWeatherHome.setHasFixedSize(true)
                mainAdapter?.notifyDataSetChanged()

                val description = it.current?.weather?.get(0)?.description
                val dt = it.current?.dt

                binding.tvTempeatur.text =
                    String.format(Locale.getDefault(), "%.0f°${ApiUnits.tempUnit}", it.current?.temp)
                binding.feelsLike.text =
                    String.format(Locale.getDefault(), "%.0f°${ApiUnits.tempUnit}", it.current?.feelsLike)

                val geocoderAddres= Geocoder(this, Locale(sharedPref.getLanguage.toString()))

                try {
                    if (sharedPref.getLanguage.toString()=="ar"){
                        address = geocoderAddres.getFromLocation(it.lat,it.lon,1)[0].countryName ?: it.timezone.toString()
                    }else{
                        address = geocoderAddres.getFromLocation(it.lat,it.lon,1)[0].adminArea ?: it.timezone.toString()
                        address+=",${geocoderAddres.getFromLocation(it.lat,it.lon,1)[0].countryName ?: it.timezone.toString()}"
                    }
                }catch (e: IOException){
                    e.printStackTrace()
                }

                binding.location.text = address
                binding.tvWeather.text = description.toString()
                binding.windSpeedTxt.text = it.current?.windSpeed.toString() + " ${ApiUnits.WindSpeedUnit}"
                binding.humidityTxt.text = it.current?.humidity.toString() + " %"
                binding.pressure.text = it.current?.pressure.toString() + " hpa"
                binding.clouds.text = it.current?.clouds.toString() + " %"

                val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale(lang))
                val format = simpleDateFormat.format(dt?.times(1000L))
                binding.DateTxt.text = format

                when (it.current?.weather?.get(0)?.icon) {
                    "04d" -> {
                        binding.iconTemp.setAnimation(R.raw.broken_clouds)
                    }
                    "04n" -> {
                        binding.iconTemp.setAnimation(R.raw.broken_clouds)
                    }
                    "10d" -> {
                        binding.iconTemp.setAnimation(R.raw.light_rain)
                    }
                    "10n" -> {
                        binding.iconTemp.setAnimation(R.raw.light_rain)
                    }
                    "09d" -> {
                        binding.iconTemp.setAnimation(R.raw.heavy_intentsity)
                    }
                    "09n" -> {
                        binding.iconTemp.setAnimation(R.raw.heavy_intentsity)
                    }
                    "03d" -> {
                        binding.iconTemp.setAnimation(R.raw.overcast_clouds)
                    }
                    "03n" -> {
                        binding.iconTemp.setAnimation(R.raw.overcast_clouds)
                    }

                    "02d" -> {
                        binding.iconTemp.setAnimation(R.raw.few_clouds)
                    }
                    "02n" -> {
                        binding.iconTemp.setAnimation(R.raw.few_clouds)
                    }

                    "01d" -> {
                        binding.iconTemp.setAnimation(R.raw.clear_sky)
                    }
                    "01n" -> {
                        binding.iconTemp.setAnimation(R.raw.clear_sky)
                    }
                    "11d" -> {
                        binding.iconTemp.setAnimation(R.raw.thunderstorm)
                    }
                    "11n" -> {
                        binding.iconTemp.setAnimation(R.raw.thunderstorm)
                    }
                    "13d" -> {
                        binding.iconTemp.setAnimation(R.raw.snow)
                    }
                    "13n" -> {
                        binding.iconTemp.setAnimation(R.raw.snow)
                    }
                    "50d" -> {
                        binding.iconTemp.setAnimation(R.raw.mist)
                    }
                    "50n" -> {
                        binding.iconTemp.setAnimation(R.raw.mist)
                    }

                    else -> {
                        binding.iconTemp.setAnimation(R.raw.unknown)
                    }
                }


            }

        })



    }
}