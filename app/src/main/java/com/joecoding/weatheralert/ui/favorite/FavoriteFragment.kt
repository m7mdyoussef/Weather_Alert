package com.joecoding.weatheralert.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joecoding.weatheralert.R
import com.joecoding.weatheralert.adapter.FavoriteAdapter
import com.joecoding.weatheralert.databinding.FragmentFavoriteBinding
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.FavoriteModel
import com.joecoding.weatheralert.ui.favoriteDetails.FavoriteDetailsActivity
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener
import com.tuann.floatingactionbuttonexpandable.FloatingActionButtonExpandable
import java.math.BigDecimal
import java.math.RoundingMode


class FavoriteFragment : Fragment() {

    private var _binding:FragmentFavoriteBinding?=null
    private val binding get() = _binding!!
    lateinit var fab: FloatingActionButtonExpandable
    private var placesList = mutableListOf<FavoriteModel>()
    private var favoriteAdapter: FavoriteAdapter? = null
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var intent: Intent
  //  lateinit var itemTochHelper: ItemTouchHelper.SimpleCallback


    var latDecimal: BigDecimal? = null
     var lonDecimal: BigDecimal? = null
     var address: String? = null

    private lateinit var swipedLat: String
    private lateinit var swipedLng: String



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        _binding=FragmentFavoriteBinding.inflate(inflater,container,false)

                return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteViewModel = ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)
            .create(FavoriteViewModel::class.java)

        fab = binding.fab
        fab.setOnClickListener {
            fab.collapse(true)
            showAutoCompleteBar()
        }

        //update RecyclerView
        favoriteViewModel.fetchFavorite().observe(viewLifecycleOwner, Observer {

            if(it != null) {
                placesList = it as MutableList<FavoriteModel>
                favoriteAdapter = FavoriteAdapter(placesList, favoriteViewModel)
                // ItemTouchHelper(itemTochHelper).attachToRecyclerView(binding.recyclerView)
                binding.recyclerView.adapter = favoriteAdapter
                binding.recyclerView.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                binding.recyclerView.setHasFixedSize(true)
                favoriteAdapter?.notifyDataSetChanged()
            }
        })




        // fetch weather data when click to search item
        favoriteViewModel.getWeatherFromFavorite(latDecimal.toString(),lonDecimal.toString())

        // intent to details activity when click to item
        intent = Intent(activity, FavoriteDetailsActivity::class.java)
        favoriteViewModel.getNavigation().observe(viewLifecycleOwner, Observer {
            //it = placesList item clicked data --> [lat,lng] in favoriteAdapter
            if(it != null) {
                intent.putExtra("lat", it[0])
                intent.putExtra("lng", it[1])

                activity?.startActivity(intent)
            }
        })



/*
        itemTochHelper = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                placesList.removeAt(viewHolder.adapterPosition)

                favoriteAdapter?.notifyDataSetChanged()
                favoriteAdapter?.notifyItemRemoved(viewHolder.adapterPosition)

                // get lat lng for item to pass it to swap for delete item
//                favoriteViewModel.getNavigation().observe(viewLifecycleOwner, Observer {
//                    favoriteViewModel.deleteItem(it[0],it[1])
//                })


            }
        }

*/
    }


    private fun showAutoCompleteBar(){

        binding.placeAutoCompleteFrag.visibility= View.VISIBLE
        val autocompleteFragment = PlaceAutocompleteFragment.newInstance("pk.eyJ1IjoibW9oYW1lZHlvdXNzZWYxOTk5MiIsImEiOiJja2xsZGFxOTQzbDNwMnZzODVya3kyZWk3In0.u6G62r1JYlNDwCZxEdDrrA" )
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.add(R.id.place_autoComplete_Frag, autocompleteFragment, "AUTOCOMPLETE_FRAGMENT_TAG")
        transaction?.commit()

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(carmenFeature: CarmenFeature) {

                // TODO: Use the longitude and latitude and place name

               val latitude = carmenFeature.center()?.latitude()
               val longitude = carmenFeature.center()?.longitude()

                latDecimal = latitude?.let { BigDecimal(it).setScale(4, RoundingMode.HALF_DOWN) }
                lonDecimal = longitude?.let { BigDecimal(it).setScale(4,RoundingMode.HALF_DOWN) }
                address = carmenFeature.text().toString()

                val favModel: FavoriteModel = FavoriteModel(address,latDecimal.toString(),lonDecimal.toString())
                //insert to fav places table
                favoriteViewModel.insertFavorite(favModel)
                // insert to data table
                favoriteViewModel.insertFavoriteToDataBase(latDecimal.toString(),lonDecimal.toString())

                Toast.makeText(context,"latitude ${carmenFeature.center()?.latitude()} \n" +
                        " longitude ${carmenFeature.center()?.longitude() } \n" +
                        "location  ${carmenFeature.text()} \n"
                    , Toast.LENGTH_LONG).show()
                fab.expand()
                activity?.supportFragmentManager?.beginTransaction()?.remove(autocompleteFragment)?.commit()
                binding.placeAutoCompleteFrag.visibility= View.GONE
            }

            override fun onCancel() {
                fab.expand()
                activity?.supportFragmentManager?.beginTransaction()?.remove(autocompleteFragment)?.commit()
                binding.placeAutoCompleteFrag.visibility= View.GONE
            }
        })


    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("destroyyyyyyyyyyyyyyy", "onDestroy:tttttttttttttttttttttttt ")
    }


    }


