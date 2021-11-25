package com.example.feature_set_location

import android.net.Uri
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
            val uri = Uri.parse("app://bottom_navigation")
            findNavController().navigate(uri)
        }
    }


    companion object {
        const val CITY_KEY = "CITY_KEY"
    }
}