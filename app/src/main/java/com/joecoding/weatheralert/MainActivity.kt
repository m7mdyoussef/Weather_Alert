package com.joecoding.weatheralert

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
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
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    lateinit var bnv_Main: MeowBottomNavigation

    private val homeFragment = HomeFragment()
    private val alarmfragment = AlarmFragment()
    private val favoriteFragment = FavoriteFragment()
    private val settingFragment = SettingFragment()

    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = homeFragment

    lateinit var fabNextDays : ExtendedFloatingActionButton





    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        bnv_Main = findViewById(R.id.bnv_Main)
        fabNextDays = findViewById(R.id.fabNextDays)
        bnv_Main.add(MeowBottomNavigation.Model(1,R.drawable.homed))
        bnv_Main.add(MeowBottomNavigation.Model(2,R.drawable.alarm))
        bnv_Main.add(MeowBottomNavigation.Model(3,R.drawable.favorite))
        bnv_Main.add(MeowBottomNavigation.Model(4,R.drawable.settings))
        bnv_Main.show(1, true)

        fragmentManager.beginTransaction().apply {
            add(R.id.frame, alarmfragment).hide(alarmfragment)
            add(R.id.frame, favoriteFragment).hide(favoriteFragment)
            add(R.id.frame, settingFragment).hide(settingFragment)
            add(R.id.frame, homeFragment)
        }.commit()


       // replace(HomeFragment())

        bnv_Main.setOnClickMenuListener { model: MeowBottomNavigation.Model ->
            when (model.id) {
                1 ->{
                    fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit()
                    activeFragment = homeFragment
                    true

                   // replace(HomeFragment())
                    fabNextDays.visibility= View.VISIBLE
                }
                2 ->{
                    fragmentManager.beginTransaction().hide(activeFragment).show(alarmfragment).commit()
                    activeFragment = alarmfragment
                    true
                   // replace(AlarmFragment())
                    fabNextDays.visibility= View.INVISIBLE
                }
                3 ->{
                    fragmentManager.beginTransaction().hide(activeFragment).show(favoriteFragment).commit()
                    activeFragment = favoriteFragment
                    true
                   // replace(FavoriteFragment())
                    fabNextDays.visibility= View.INVISIBLE
                }
                4 ->{
                    fragmentManager.beginTransaction().hide(activeFragment).show(settingFragment).commit()
                    activeFragment = settingFragment
                    true
                   // replace(SettingFragment())
                    fabNextDays.visibility= View.INVISIBLE
                }
                else -> false

            }
           // null
        }



        val fragmentNextDays = FragmentNextDays.newInstance("FragmentNextDays")
        fabNextDays.setOnClickListener { fragmentNextDays.show(supportFragmentManager, fragmentNextDays.tag) }




    }

    /////////////////////////////////////////////////////////////

/*
    private fun replace(fragment: Fragment) {

        val transaction =supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
    }
    */
    override fun onBackPressed() {
        //  if(MeowBottomNavigation.Model.id > 1)
        super.onBackPressed()
    }

  }