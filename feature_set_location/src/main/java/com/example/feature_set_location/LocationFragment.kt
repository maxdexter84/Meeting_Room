package com.example.feature_set_location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.example.feature_set_location.databinding.LocationFragmentBinding

class LocationFragment: Fragment() {

    lateinit var binding: LocationFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) { key, bundle ->
            val result = bundle.getString("bundleKey")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectLayoutLocationFragment.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("android-app://com.meetingroom.app/countryFragment".toUri())
                .build()
            findNavController().navigate(request)
        }
    }
}