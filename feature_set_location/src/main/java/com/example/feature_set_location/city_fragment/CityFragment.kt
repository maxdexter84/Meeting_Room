package com.example.feature_set_location.city_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.feature_set_location.databinding.CityFragmentBinding

class CityFragment: Fragment() {

    lateinit var binding: CityFragmentBinding
    private val cityAdapter = CityAdapter()

    //Instead of a real list from server
    private val countryList = mutableListOf("Dnipro", "Kyiv", "Odessa")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CityFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewCityFragment.adapter = cityAdapter
        cityAdapter.cities = countryList

        binding.toolBarLocationFragment.arrowBackLocationFragment.setOnClickListener {
            findNavController().popBackStack()
        }

        requireActivity().supportFragmentManager.setFragmentResult("requestKey", bundleOf("bundleKey" to
        cityAdapter.selectedCity))
//            .setFragmentResult("requestKey", bundleOf("bundleKey" to ))
//        cityAdapter.onItemClick =
    }
}