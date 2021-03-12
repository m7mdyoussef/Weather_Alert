package com.joecoding.weatheralert.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButtonToggleGroup
import com.joecoding.weatheralert.R
import com.joecoding.weatheralert.databinding.FragmentSettingBinding
import com.joecoding.weatheralert.providers.SharedPreferencesProvider
import kotlinx.android.synthetic.main.fragment_setting.view.*


class SettingFragment : Fragment() {

    lateinit var viewModel: SettingsViewModel
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    lateinit var sharedPref: SharedPreferencesProvider


    private var language:String="en"
    private var units:String="metric"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)
            .create(SettingsViewModel::class.java)

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
                        showToast("metric")
                    }
                    R.id.btnImperial -> {
                        units="imperial"
                        showToast("imperial")
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
                        showToast("Arabic")

                    }
                    R.id.englishBtn -> {
                        language="en"
                        showToast("English")

                    }
                }
                sharedPref.setLanguage(language)
            }

        }

        binding.addAlarmBtn.setOnClickListener(View.OnClickListener {
            requireActivity().recreate()
        })


    }

    private fun showToast(str: String) {
        Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
    }

}