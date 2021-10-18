package com.example.feature_set_location

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.feature_set_location.databinding.LocationFragmentBinding
import com.meeringroom.ui.view.base_classes.BaseFragment

class LocationFragment : BaseFragment<LocationFragmentBinding>(LocationFragmentBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.citySelectText.text = arguments?.getString(CITY_KEY) ?: ""

        binding.selectLayoutLocationFragment.setOnClickListener {
            findNavController().navigate(R.id.action_locationFragment_to_countryFragment)
        }

        binding.confirmLocationFragment.setOnClickListener {
            findNavController().navigate(R.id.action_locationFragment_to_landing_screen_navigation)
        }
    }

    companion object {
        const val CITY_KEY = "CITY_KEY"
    }
}