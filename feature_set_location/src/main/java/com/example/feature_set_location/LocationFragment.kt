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
            navigateToDeepLink(resources.getString(R.string.deeplink_uri_bottom_navigation))
        }
    }


    companion object {
        const val CITY_KEY = "CITY_KEY"
    }

    private fun navigateToDeepLink(link: String) {
        val uri = Uri.parse(link)
        findNavController().navigate(uri)
    }
}