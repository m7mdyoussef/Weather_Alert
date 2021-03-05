package com.joecoding.weatheralert.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.joecoding.weatheralert.databinding.FragmentSettingBinding
import com.joecoding.weatheralert.providers.SharedPreferencesProvider


class SettingFragment : Fragment() {

    lateinit var viewModel: SettingsViewModel
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    lateinit var sharedPref: SharedPreferencesProvider


    var imperialIsCheched:Boolean = false
    var metricIsCheched:Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)
            .create(SettingsViewModel::class.java)

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val c = activity?.getSharedPreferences("imperial",Context.MODE_PRIVATE)

        sharedPref = SharedPreferencesProvider(requireContext())

       // sharedPref.isUnitsConvertedToImperial

        binding.ImperialChecked.setOnClickListener(View.OnClickListener {
                if(imperialIsCheched){
                    binding.ImperialChecked.speed=-1f
                    binding.ImperialChecked.playAnimation()
                    imperialIsCheched=false
                    sharedPref.convertUnitsToImperial(false)

                }else{
                    binding.ImperialChecked.speed=1f
                    binding.ImperialChecked.playAnimation()
                    binding.MetricChecked.speed=-1f
                    binding.MetricChecked.playAnimation()
                    imperialIsCheched=true
                    metricIsCheched=false
                    sharedPref.convertUnitsToImperial(true)
                    sharedPref.convertUnitsToMetric(false)
                }



        })

        binding.MetricChecked.setOnClickListener(View.OnClickListener {
            if(metricIsCheched){
                binding.MetricChecked.speed=-1f
                binding.MetricChecked.playAnimation()
                metricIsCheched=false
                sharedPref.convertUnitsToMetric(false)
            }else{
                binding.MetricChecked.speed=1f
                binding.MetricChecked.playAnimation()
                binding.ImperialChecked.speed=-1f
                binding.ImperialChecked.playAnimation()
                metricIsCheched=true
                imperialIsCheched=false
                sharedPref.convertUnitsToImperial(false)
                sharedPref.convertUnitsToMetric(true)

            }
        })


    }

}