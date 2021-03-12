package com.joecoding.weatheralert

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.joecoding.weatheralert.providers.MyContextWrapper
import com.joecoding.weatheralert.providers.SharedPreferencesProvider
import com.joecoding.weatheralert.ui.alarm.AlarmFragment
import com.joecoding.weatheralert.ui.favorite.FavoriteFragment
import com.joecoding.weatheralert.ui.home.FragmentNextDays
import com.joecoding.weatheralert.ui.home.HomeFragment
import com.joecoding.weatheralert.ui.settings.SettingFragment

class MainActivity : AppCompatActivity() {

    lateinit var bnv_Main: MeowBottomNavigation

    lateinit var fabNextDays : ExtendedFloatingActionButton

    lateinit var sharedPref: SharedPreferencesProvider

    override fun attachBaseContext(newBase: Context?) {
        sharedPref = SharedPreferencesProvider(newBase!!)
        val lang = sharedPref.getLanguage

        super.attachBaseContext(MyContextWrapper.wrap(newBase,lang))

    }


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
        }

        val fragmentNextDays = FragmentNextDays.newInstance("FragmentNextDays")
        fabNextDays.setOnClickListener { fragmentNextDays.show(supportFragmentManager, fragmentNextDays.tag) }

    }



    private fun replace(fragment: Fragment) {

        val transaction =supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        //  if(MeowBottomNavigation.Model.id > 1)
        super.onBackPressed()
    }

  }