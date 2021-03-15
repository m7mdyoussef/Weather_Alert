package com.joecoding.weatheralert.ui.settings

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.joecoding.weatheralert.MainActivity
import com.joecoding.weatheralert.R
import com.joecoding.weatheralert.databinding.FragmentSettingBinding
import com.joecoding.weatheralert.providers.SharedPreferencesProvider
import com.joecoding.weatheralert.ui.welcom.SplashScreen

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    lateinit var sharedPref: SharedPreferencesProvider


    private var language:String="en"
    private var units:String="metric"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        return binding.root


    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = SharedPreferencesProvider(requireContext())

        binding.toggleButtonGroupUnits.addOnButtonCheckedListener { group, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                    R.id.btnMetric -> {
                        units="metric"
                    }
                    R.id.btnImperial -> {
                        units="imperial"
                    }
                }
                sharedPref.setUnit(units)
            }
        }

        binding.toggleButtonGroupLanguage.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.arabicBtn -> {
                        language="ar"
                    }
                    R.id.englishBtn -> {
                        language="en"
                    }
                }
                sharedPref.setLanguage(language)
            }

        }

        binding.addAlarmBtn.setOnClickListener(View.OnClickListener {
            CheckStatus()
           // requireActivity().recreate()

        })

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

    @RequiresApi(Build.VERSION_CODES.M)
    fun CheckStatus(){
        if(!isConnected()) {
            showDialog()
            Toast.makeText(requireContext(), getString(R.string.connectionFailed), Toast.LENGTH_LONG).show()
        }else{
            requireActivity().recreate()
        }
    }
     fun showDialog(){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage(getString(R.string.checkInterNet))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.connect)){
                    dialog, which ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))

                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.exit)){ dialog, which ->
               // requireActivity().finish()
                startActivity(Intent(requireContext(), MainActivity::class.java)
                )
                dialog.dismiss()
            }
            .show()
    }

    private fun showToast(str: String) {
        Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
    }



}