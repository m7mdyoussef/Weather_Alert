package com.joecoding.weatheralert.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.HourlyItem
import com.joecoding.weatheralert.providers.SharedPreferencesProvider
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*

class HomeFragment : Fragment() {


    lateinit var mFusedLocationClient: FusedLocationProviderClient
    val PermissionId = 1
    lateinit var sharedPref: SharedPreferencesProvider


    private var mainAdapter: MainAdapter? = null

    lateinit var viewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPreferencesProvider(requireActivity().application)
        CheckStatus()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity().application)
        getLatestLocation()



        viewModel = ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)
            .create(HomeViewModel::class.java)

        viewModel.getWeather().observe(viewLifecycleOwner, Observer {

            if(it != null) {
                val hourly: List<HourlyItem?>? = it.hourly
                mainAdapter = MainAdapter(hourly)


           Log.d("dataaaaaaa", it.toString())

                binding.rvListWeatherHome.adapter = mainAdapter
                binding.rvListWeatherHome.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                binding.rvListWeatherHome.setHasFixedSize(true)
                mainAdapter?.notifyDataSetChanged()

                val description = it.current?.weather?.get(0)?.description
                val dt = it.current?.dt

                binding.tvTempeatur.text =
                    String.format(Locale.getDefault(), "%.0f°C", it.current?.temp)
                binding.feelsLike.text =
                    String.format(Locale.getDefault(), "%.0f°C", it.current?.feelsLike)
                binding.location.text = it.timezone.toString()
                binding.tvWeather.text = description.toString()
                binding.windSpeedTxt.text = it.current?.windSpeed.toString() + " m/s"
                binding.humidityTxt.text = it.current?.humidity.toString() + " %"
                binding.pressure.text = it.current?.pressure.toString() + " hpa"
                binding.clouds.text = it.current?.clouds.toString() + " %"

                val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.ENGLISH)
                val format = simpleDateFormat.format(dt?.times(1000L))
                binding.DateTxt.text = format

                when (description) {
                    "broken clouds" -> {
                        binding.iconTemp.setAnimation(R.raw.broken_clouds)

                    }
                    "light rain" -> {
                        binding.iconTemp.setAnimation(R.raw.light_rain)
                    }
                    "haze" -> {
                        binding.iconTemp.setAnimation(R.raw.broken_clouds)
                    }
                    "overcast clouds" -> {
                        binding.iconTemp.setAnimation(R.raw.overcast_clouds)
                    }
                    "moderate rain" -> {
                        binding.iconTemp.setAnimation(R.raw.moderate_rain)
                    }
                    "few clouds" -> {
                        binding.iconTemp.setAnimation(R.raw.few_clouds)
                    }
                    "heavy intensity rain" -> {
                        binding.iconTemp.setAnimation(R.raw.heavy_intentsity)
                    }
                    "clear sky" -> {
                        binding.iconTemp.setAnimation(R.raw.clear_sky)
                    }
                    "scattered clouds" -> {
                        binding.iconTemp.setAnimation(R.raw.scattered_clouds)
                    }
                    else -> {
                        binding.iconTemp.setAnimation(R.raw.unknown)
                    }
                }

            }
        })

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    @SuppressLint("MissingPermission")
    fun getLatestLocation() {
        if (isPermissionGranted()) {
            if (checkLocation()) {
                val locationRequest = LocationRequest()
                with(locationRequest) {
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    interval = 1000
                }
                val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity().application)
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            } else {

                val firstTimeLaunch = sharedPref.isFirstTimeLaunch
                if(firstTimeLaunch){
                    val firstTimeLocationEnabled = sharedPref.isFirstTimeLocationEnabled
                    if(firstTimeLocationEnabled) {
                        showErrorDialog("Location","Kindly enable Location to use Application properly")
                    }
                }

            }
        } else {
            requestPermission()
        }
    }



    private fun showErrorDialog(alertTitle: String, message: String) {
        val builder = AlertDialog.Builder(requireActivity().application)
        builder.setTitle(alertTitle)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Enable Location"){
                    dialog, which ->
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                dialog.dismiss()
            }
            .setNegativeButton("Exit"){
                    dialog, which ->  requireActivity().finish()
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

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            // TODO use current location long and lat
            val lonDecimal = BigDecimal(location.longitude).setScale(4, RoundingMode.HALF_DOWN)
            val latDecimal = BigDecimal(location.latitude).setScale(4, RoundingMode.HALF_DOWN)
            sharedPref.setLatLong("$latDecimal","$lonDecimal")
        }
    }


    private fun enableLocation() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            1)
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
            Toast.makeText(requireActivity(), "connection failed", Toast.LENGTH_LONG)
                .show()
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun isConnected(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                //   Log.i(Constants.LOG_TAG, "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                //  Log.i(Constants.LOG_TAG, "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                //   Log.i(Constants.LOG_TAG, "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
    private fun showDialog(){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Please check the Internet to proceed further")
            .setCancelable(false)
            .setPositiveButton("Connect"){
                    dialog, which ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                dialog.dismiss()
            }
            .setNegativeButton("Exit"){
                    dialog, which ->  requireActivity().finish()
                dialog.dismiss()
            }
            .show()
    }
}