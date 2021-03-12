package com.joecoding.weatheralert.ui.home


import android.graphics.Color

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.joecoding.weatheralert.adapter.NextDayAdapter
import com.joecoding.weatheralert.databinding.FragmentNextDayBinding
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.DailyItem


class FragmentNextDays : BottomSheetDialogFragment(){


    var nextDayAdapter: NextDayAdapter? = null

    lateinit var viewModel: HomeViewModel
    private var _binding: FragmentNextDayBinding? = null
    private val binding get() = _binding!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View? {
        _binding = FragmentNextDayBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        viewModel.getWeather().observe(viewLifecycleOwner, Observer {

            if (it != null){
                val daily: List<DailyItem?>? = it.daily

                binding.rvDailyWeather.showShimmerAdapter()

                nextDayAdapter = NextDayAdapter(requireActivity(), daily)
                binding.rvDailyWeather.adapter=nextDayAdapter
                binding.rvDailyWeather.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                binding.rvDailyWeather.setHasFixedSize(true)
                nextDayAdapter?.notifyDataSetChanged()
                binding.rvDailyWeather.hideShimmerAdapter()
            }





        })

        return binding.root
    }

    companion object {
        fun newInstance(string: String?): FragmentNextDays {
            val fragmentNextDays =
                FragmentNextDays()
            val args = Bundle()
            args.putString("string", string)
            fragmentNextDays.arguments = args
            return fragmentNextDays
        }
    }
}