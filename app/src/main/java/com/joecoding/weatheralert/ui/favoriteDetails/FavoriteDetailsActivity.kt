package com.joecoding.weatheralert.ui.favoriteDetails

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout
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

    private var currentWeatherFav: CurrentWeatherModel? = null
    var lat: String? = null
    var lng: String? = null
    override fun attachBaseContext(newBase: Context?) {
        sharedPref = SharedPreferencesProvider(newBase!!)
        lang = sharedPref.getLanguage!!
        super.attachBaseContext(MyContextWrapper.wrap(newBase,lang))
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fabNextDaysFav = findViewById(R.id.fabNextDaysFav)

        lat = intent.getStringExtra("lat")

        lng = intent.getStringExtra("lng")

        val fragmentNextDaysFav = FragmentNextDaysFav.newInstance("FragmentNextDays")
        fabNextDaysFav?.setOnClickListener {
            fragmentNextDaysFav.show(supportFragmentManager, fragmentNextDaysFav.tag)
        }

        favoriteDetailsViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(FavoriteDetailsViewModel::class.java)

        //fetch favorite data from DB
        getfavData(lat,lng)
    }

    private fun getfavData(lati:String?, longi:String?){
        favoriteDetailsViewModel.getFavoriteWeatherData(lati,longi).observe(this, Observer {
            currentWeatherFav=it
            if(currentWeatherFav != null) {
                binding.favprogress.visibility= View.GONE
                binding.favView.visibility=View.VISIBLE
                upDateFavRecyclerView()
                setDataToFavHomeScreen()
                setIconsFav()
            }else{
                binding.favprogress.visibility= View.VISIBLE
                binding.favView.visibility=View.GONE
            }

        })
    }

    private fun upDateFavRecyclerView(){
        val hourly: List<HourlyItem?>? = currentWeatherFav!!.hourly
        mainAdapter = MainAdapter(this,hourly)
        binding.rvListWeatherHome.adapter = mainAdapter
        binding.rvListWeatherHome.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvListWeatherHome.setHasFixedSize(true)
        mainAdapter?.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    private fun setDataToFavHomeScreen(){
        val description = currentWeatherFav!!.current?.weather?.get(0)?.description
        val dt = currentWeatherFav!!.current?.dt
        binding.tvTempeatur.text = String.format(Locale.getDefault(), "%.0f°${ApiUnits.tempUnit}", currentWeatherFav!!.current?.temp)
        binding.feelsLike.text = String.format(Locale.getDefault(), "%.0f°${ApiUnits.tempUnit}", currentWeatherFav!!.current?.feelsLike)
        binding.location.text = geoCoderAddressFav()
        binding.tvWeather.text = description.toString()
        binding.windSpeedTxt.text = currentWeatherFav!!.current?.windSpeed.toString() + " ${ApiUnits.WindSpeedUnit}"
        binding.humidityTxt.text = currentWeatherFav!!.current?.humidity.toString() + " %"
        binding.pressure.text = currentWeatherFav!!.current?.pressure.toString() + " hpa"
        binding.clouds.text = currentWeatherFav!!.current?.clouds.toString() + " %"
        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale(lang))
        val format = simpleDateFormat.format(dt?.times(1000L))
        binding.DateTxt.text = format
    }

    private fun geoCoderAddressFav(): String{
        var addressFav: String = ""
        val geocoderAddres=Geocoder(this, Locale(sharedPref.getLanguage.toString()))
        try {
            if (sharedPref.getLanguage.toString()=="ar"){
                addressFav = geocoderAddres.getFromLocation(currentWeatherFav!!.lat,currentWeatherFav!!.lon,1)[0].countryName ?: currentWeatherFav!!.timezone.toString()
            }else{
                addressFav = geocoderAddres.getFromLocation(currentWeatherFav!!.lat,currentWeatherFav!!.lon,1)[0].adminArea ?: currentWeatherFav!!.timezone.toString()
                addressFav+=",${geocoderAddres.getFromLocation(currentWeatherFav!!.lat,currentWeatherFav!!.lon,1)[0].countryName ?: currentWeatherFav!!.timezone.toString()}"
            }
        }catch (e: IOException){
            e.printStackTrace()
        }
        return addressFav
    }

    private fun setIconsFav(){
        when (currentWeatherFav!!.current?.weather?.get(0)?.icon) {
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

}