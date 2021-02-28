package com.joecoding.weatheralert.ui.home

import android.location.Address
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azhar.weatherapp.adapter.MainAdapter
import com.azhar.weatherapp.model.ModelMain
import com.google.android.gms.location.FusedLocationProviderClient
import com.joecoding.weatheralert.R
import com.joecoding.weatheralert.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.ArrayList

class HomeFragment : Fragment() {
    private var mainAdapter: MainAdapter? = null
    private val modelMain: MutableList<ModelMain> = ArrayList()


    lateinit var viewModel: HomeViewModel
    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        mainAdapter = MainAdapter(modelMain)
        binding.rvListWeatherHome.adapter=mainAdapter
        binding.rvListWeatherHome.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvListWeatherHome.setHasFixedSize(true)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)
            .create(HomeViewModel::class.java)



        viewModel.getWeather().observe(viewLifecycleOwner, Observer {


        binding.DateTxt.text= it.daily?.get(0)?.weather?.get(0)?.description.toString()

            Log.d("dataaaaaaa",it.toString() )

        })

    }
}