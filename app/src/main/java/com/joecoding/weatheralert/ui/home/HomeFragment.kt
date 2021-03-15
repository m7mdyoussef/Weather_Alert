package com.joecoding.weatheralert.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.joecoding.weatheralert.R
import com.joecoding.weatheralert.adapter.MainAdapter
import com.joecoding.weatheralert.databinding.FragmentHomeBinding
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.weatherModel.CurrentWeatherModel
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.HourlyItem
import com.joecoding.weatheralert.network.ApiUnits
import com.joecoding.weatheralert.providers.SharedPreferencesProvider
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {


    lateinit var mFusedLocationClient: FusedLocationProviderClient
    val PermissionId = 1
    lateinit var sharedPref: SharedPreferencesProvider

    private var mainAdapter: MainAdapter? = null

    lateinit var viewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    var lonDecimal: BigDecimal? = null
    var latDecimal: BigDecimal? = null
    private var currentWeather: CurrentWeatherModel? = null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        // Checking for first time launch - before calling setContentView()
        sharedPref = SharedPreferencesProvider(requireContext())
        if (sharedPref.isFirstTimeLaunch) { // if not the first time
            CheckStatus()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



/*
        viewModel.getWeather().observe(viewLifecycleOwner, Observer {
            Log.d("testtttttttttttt", "insideGetWeatherViewModel ")
            currentWeather = it
            if(it != null) {
                Log.d("testtttttttttttt", "insideGetWeatherViewModel 2222222")
                binding.refresh.isRefreshing=false
                updateRecyclerView()
                setDataToHomeScreen()
                setIcons()
                refreshData()
            }
        })
*/
    }






    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        sharedPref= SharedPreferencesProvider(requireActivity())
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity().application)

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(HomeViewModel::class.java)
        getLatestLocation()

        viewModel.getWeather().observe(viewLifecycleOwner, Observer {
            Log.d("testtttttttttttt", "outsideGetWeatherViewModel 2222222")
            currentWeather = it
            if(currentWeather != null) {
                binding.progressBar.visibility=View.GONE
                binding.screenView.visibility=View.VISIBLE
                Log.d("testtttttttttttt", "insideGetWeatherViewModel 2222222")
                binding.refresh.isRefreshing=false
                updateRecyclerView()
                setDataToHomeScreen()
                setIcons()
                refreshData()
            }else{
                binding.progressBar.visibility=View.VISIBLE
                binding.screenView.visibility=View.GONE
            }
        })
        return binding.root
    }

    private fun updateRecyclerView(){
        val hourly: List<HourlyItem?>? = currentWeather!!.hourly
        mainAdapter = MainAdapter(requireContext(),hourly)
        binding.rvListWeatherHome.adapter = mainAdapter
        binding.rvListWeatherHome.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvListWeatherHome.setHasFixedSize(true)
        mainAdapter?.notifyDataSetChanged()
    }

    private fun refreshData(){
        binding.refresh.setOnRefreshListener(WaveSwipeRefreshLayout.OnRefreshListener {
            viewModel.refreshCurrentLocation()
            // getLatestLocation()
            Toast.makeText(requireActivity(),getString(R.string.refresh),Toast.LENGTH_SHORT).show()
        })

    }

    private fun geoCoderAddress(): String{
        var address: String = ""
        val geocoderAddres=Geocoder(requireContext(), Locale(sharedPref.getLanguage.toString()))
        try {
            if (sharedPref.getLanguage.toString()=="ar"){
                address = geocoderAddres.getFromLocation(currentWeather!!.lat,currentWeather!!.lon,1)[0].countryName ?: currentWeather!!.timezone.toString()
            }else{
                address = geocoderAddres.getFromLocation(currentWeather!!.lat,currentWeather!!.lon,1)[0].adminArea ?: currentWeather!!.timezone.toString()
                address+=",${geocoderAddres.getFromLocation(currentWeather!!.lat,currentWeather!!.lon,1)[0].countryName ?: currentWeather!!.timezone.toString()}"
            }
        }catch (e: IOException){
            e.printStackTrace()
        }
        return address
    }

    @SuppressLint("SetTextI18n")
    private  fun setDataToHomeScreen(){
        val description = currentWeather!!.current?.weather?.get(0)?.description
        val dt = currentWeather!!.current?.dt

        binding.tvTempeatur.text = String.format(Locale.getDefault(), "%.0f°${ApiUnits.tempUnit}", currentWeather!!.current?.temp)
        binding.feelsLike.text = String.format(Locale.getDefault(), "%.0f°${ApiUnits.tempUnit}", currentWeather!!.current?.feelsLike)

        binding.location.text = geoCoderAddress()
        binding.tvWeather.text = description.toString()
        binding.windSpeedTxt.text = currentWeather!!.current?.windSpeed.toString() + " ${ApiUnits.WindSpeedUnit}"
        binding.humidityTxt.text = currentWeather!!.current?.humidity.toString() + " %"
        binding.pressure.text = currentWeather!!.current?.pressure.toString() + " hpa"
        binding.clouds.text = currentWeather!!.current?.clouds.toString() + " %"

        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale(sharedPref.getLanguage.toString()))
        val format = simpleDateFormat.format(dt?.times(1000L))
        binding.DateTxt.text = format
    }

    private fun setIcons(){
        when (currentWeather!!.current?.weather?.get(0)?.icon) {
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

    @SuppressLint("MissingPermission")
    fun getLatestLocation() {
        if (isPermissionGranted()) {
            if (checkLocation()) {
                val locationRequest = LocationRequest()
                with(locationRequest) {
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    interval = 1000
                    numUpdates=2

                }
                val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity().application)
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                viewModel.refreshCurrentLocation()
            }
            else {
                showLocationDialog(getString(R.string.location_),getString(R.string.enablelocation))
            }
        } else {
            requestPermission()
        }
    }



    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            // TODO use current location long and lat

            lonDecimal = BigDecimal(location.longitude).setScale(4, RoundingMode.HALF_DOWN)
            latDecimal = BigDecimal(location.latitude).setScale(4, RoundingMode.HALF_DOWN)
            sharedPref.setLatLong("$latDecimal","$lonDecimal")
            Log.d("testtttttttttttt", "onLocationResult: ")


        }
    }

    private fun requestPermission() {

        var permissions= arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        requestPermissions(permissions, PermissionId)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionId) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getLatestLocation()
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun CheckStatus(){
        if(!isConnected()){
            showDialog()
            Toast.makeText(requireContext(), getString(R.string.connectionFailed), Toast.LENGTH_LONG).show()
        }else if(isConnected()){
            if (!checkLocation()){
                showLocationDialog(getString(R.string.loc),getString(R.string.enablelocation))
            }
        }
        if(isConnected() && checkLocation()){
            sharedPref.setFirstTimeLaunch(false)
        }

    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun isConnected(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }
    private fun showDialog(){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage(getString(R.string.checkInterNet))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.connect)){
                    dialog, which ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))

                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.exit)){
                    dialog, which ->  requireActivity().finish()
                dialog.dismiss()
            }
            .show()
    }
    private fun showLocationDialog(alertTitle: String, message: String) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(alertTitle)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.enablelocation_)){
                    dialog, which ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.exit)){
                    dialog, which -> requireActivity().finish()
                dialog.dismiss()
            }
            .show()

    }
    private fun isPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(requireActivity().application, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireActivity().application, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    private fun checkLocation(): Boolean {
        val locationManager = requireActivity().application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            sharedPref.setFirstTimeLocationenabled(true)
            return true
        }else{
            return false;
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}