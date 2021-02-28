package com.joecoding.weatheralert

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.azhar.weatherapp.adapter.MainAdapter
import com.azhar.weatherapp.model.ModelMain
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.joecoding.weatheralert.providers.SharedPreferencesProvider
import com.joecoding.weatheralert.ui.alarm.AlarmFragment
import com.joecoding.weatheralert.ui.favorite.FavoriteFragment
import com.joecoding.weatheralert.ui.home.FragmentNextDays
import com.joecoding.weatheralert.ui.home.HomeFragment
import com.joecoding.weatheralert.ui.settings.SettingFragment
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {


    lateinit var bnv_Main: MeowBottomNavigation
    lateinit var fabNextDays : ExtendedFloatingActionButton
    private var mainAdapter: MainAdapter? = null
    private val modelMain: MutableList<ModelMain> = ArrayList()



    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var PermissionId = 1
    var addressList: List<Address>? = null

    var long: Double = 0.0
    var lat: Double = 0.0

    lateinit var latPref: String
    lateinit var lngPref: String


    lateinit var sharedPref: SharedPreferencesProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bnv_Main = findViewById(R.id.bnv_Main)
        fabNextDays = findViewById(R.id.fabNextDays)
        bnv_Main.add(MeowBottomNavigation.Model(1,R.drawable.home))
        bnv_Main.add(MeowBottomNavigation.Model(2,R.drawable.alarm))
        bnv_Main.add(MeowBottomNavigation.Model(3,R.drawable.favorite))
        bnv_Main.add(MeowBottomNavigation.Model(4,R.drawable.settings))
        bnv_Main.show(1, true)
        replace(HomeFragment())
        bnv_Main.setOnClickMenuListener { model: MeowBottomNavigation.Model ->
            when (model.id) {
                1 ->{
                    replace(HomeFragment())
                    fabNextDays.visibility= View.VISIBLE

                }
                2 ->{
                    replace(AlarmFragment())
                    fabNextDays.visibility= View.INVISIBLE
                }
                3 ->{
                    replace(FavoriteFragment())
                    fabNextDays.visibility= View.INVISIBLE

                }
                4 ->{
                    replace(SettingFragment())
                    fabNextDays.visibility= View.INVISIBLE

                }
            }
            null
        }

        val fragmentNextDays = FragmentNextDays.newInstance("FragmentNextDays")
        fabNextDays.setOnClickListener {
            fragmentNextDays.show(supportFragmentManager, fragmentNextDays.tag)
           // getLastLocation()
        }

        sharedPref = SharedPreferencesProvider(this)


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        val latLong = sharedPref.latLong
        latPref = latLong[0]
        lngPref = latLong[1]


    }

    @SuppressLint("MissingPermission")
    fun getLastLocation () {
            if (checkPremisions()) {
                if (isLocationEnabled) {
                    mFusedLocationClient.lastLocation
                        .addOnCompleteListener { task ->
                            val location = task.result
                            if (location == null) {
                                requestNewLocationData()
                            } else {

                                long = location.longitude
                                lat = location.latitude

                                sharedPref.setLatLong("$lat","$long")

                                val geocoder =
                                    Geocoder(this@MainActivity) //to change number of latitude, longitude to text address
                                try {
                                    // max. one adress from alot ..  most acurate.
                                    addressList = geocoder.getFromLocation(
                                        location.latitude,
                                        location.longitude,
                                        1
                                    )
                                } catch (e: IOException) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "connection failed",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show() // geocoder deals with internet to change from nums. to text adress so catch connections.
                                }


                                Toast.makeText(
                                    this@MainActivity,
                                    """my longitude : $lngPref  my latitude : $latPref location is: """ + addressList!![0]
                                        .getAddressLine(0), Toast.LENGTH_LONG).show()

//                                Toast.makeText(
//                                    this@MainActivity,
//                                    """my longitude : ${location.longitude}my latitude : ${location.latitude}my location is: """ + addressList!![0]
//                                        .getAddressLine(0), Toast.LENGTH_LONG).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Turn on Location", Toast.LENGTH_SHORT).show()
                    val inet = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(inet)
                }
            } else {
                requestPermisions()
            }
        }


    private fun requestPermisions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION), PermissionId)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionId) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationReq = LocationRequest()
        locationReq.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationReq.interval = 0
        locationReq.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            locationReq,
            locationCallBack,
            Looper.myLooper()
        )
    }

    private val locationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation = locationResult.lastLocation
            val geocoder =
                Geocoder(this@MainActivity) //to change number of latitude, longitude to text address
            try {
                // max. one adress from alot ..  most acurate.
                addressList = geocoder.getFromLocation(
                    lastLocation.latitude,
                    lastLocation.longitude,
                    1
                )
            } catch (e: IOException) {
                Toast.makeText(this@MainActivity, "connection failed", Toast.LENGTH_SHORT)
                    .show() // geocoder deals with internet to change from nums. to text adress so catch connections.
            }
//            Toast.makeText(
//                this@MainActivity,
//                """my longitude : ${lastLocation.longitude}my latitude : ${lastLocation.latitude}my location is: ${addressList!![0].getAddressLine(0)}""",
//                Toast.LENGTH_LONG).show()
        }
    }


    private val isLocationEnabled: Boolean get() {
            val locationManager =
                getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }

    private fun checkPremisions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private fun replace(fragment: Fragment) {
        val transaction =supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
    }

}