package com.joecoding.weatheralert.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.joecoding.weatheralert.R
import com.joecoding.weatheralert.databinding.FragmentFavoriteBinding
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.core.constants.Constants
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener


class FavoriteFragment : Fragment() {

    private var _binding:FragmentFavoriteBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showAutoCompleteBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
       _binding=FragmentFavoriteBinding.inflate(inflater,container,false)


        return binding.root

    }

    private fun showAutoCompleteBar(){
        binding.placeAutoCompleteFrag.visibility= View.VISIBLE
        val autocompleteFragment = PlaceAutocompleteFragment.newInstance(
            "pk.eyJ1IjoibW9oYW1lZHlvdXNzZWYxOTk5MiIsImEiOiJja2xsZGFxOTQzbDNwMnZzODVya3kyZWk3In0.u6G62r1JYlNDwCZxEdDrrA" )
        var transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.add(R.id.place_autoComplete_Frag, autocompleteFragment, "AUTOCOMPLETE_FRAGMENT_TAG")
        transaction?.commit()

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(carmenFeature: CarmenFeature) {
                // TODO: Use the longitude and latitude
                Toast.makeText(context,"latitude ${carmenFeature.center()?.latitude()} \n longitude ${carmenFeature.center()?.longitude()}"
                    , Toast.LENGTH_LONG).show()
                activity?.supportFragmentManager?.beginTransaction()?.remove(autocompleteFragment)?.commit()
                binding.placeAutoCompleteFrag.visibility= View.GONE
            }

            override fun onCancel() {
                activity?.supportFragmentManager?.beginTransaction()?.remove(autocompleteFragment)?.commit()
                binding.placeAutoCompleteFrag.visibility= View.GONE
            }
        })
    }


    }


