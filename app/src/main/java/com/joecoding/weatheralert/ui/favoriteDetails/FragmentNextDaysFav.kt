package com.joecoding.weatheralert.ui.favoriteDetails


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.joecoding.weatheralert.adapter.NextDayAdapter
import com.joecoding.weatheralert.databinding.FragmentNextDayFavBinding
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.DailyItem
import com.joecoding.weatheralert.providers.SharedPreferencesProvider
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class FragmentNextDaysFav : BottomSheetDialogFragment(){


    var nextDayAdapter: NextDayAdapter? = null

    lateinit var viewModel: FavoriteDetailsViewModel
    private var _binding: FragmentNextDayFavBinding? = null
    private val binding get() = _binding!!

    private lateinit var latLongFav: Array<String?>

    lateinit var sharedPref: SharedPreferencesProvider

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sharedPref = SharedPreferencesProvider(requireContext())

        _binding = FragmentNextDayFavBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(FavoriteDetailsViewModel::class.java)

        latLongFav = sharedPref.latLongFav

        viewModel.getFavoriteWeatherData(latLongFav[0],latLongFav[1]).observe(viewLifecycleOwner, Observer {

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
        fun newInstance(string: String?): FragmentNextDaysFav {
            val fragmentNextDays =
                FragmentNextDaysFav()
            val args = Bundle()
            args.putString("string", string)
            fragmentNextDays.arguments = args
            return fragmentNextDays
        }
    }
}