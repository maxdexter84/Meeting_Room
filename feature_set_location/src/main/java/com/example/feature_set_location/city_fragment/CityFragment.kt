package com.example.feature_set_location.city_fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.example.feature_set_location.databinding.CityFragmentBinding
import com.example.feature_set_location.di.CityFragmentModule
import com.example.feature_set_location.di.DaggerCityComponent
import com.meeringroom.ui.view.base_fragment.BaseFragment
import javax.inject.Inject

class CityFragment : BaseFragment<CityFragmentBinding>(CityFragmentBinding::inflate) {

    private val cityAdapter =
        CityAdapter(onItemClick = {
            saveCity(it)
        })

    @Inject
    lateinit var viewModel: CityFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerCityComponent.builder()
            .cityFragmentModule(CityFragmentModule(this))
            .sharedPreferencesModule(SharedPreferencesModule(requireContext()))
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewCityFragment.adapter = cityAdapter
        viewModel.requestResult.observe(viewLifecycleOwner, {
            val alreadySelectedCity =
                viewModel?.getCityOfUserLocation() ?: ""
            for (city in it) {
                cityAdapter.cities += CityAdapterModel(city.name, alreadySelectedCity == city.name)
            }
        })
        binding.toolBarLocationFragment.arrowBackLocationFragment.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun saveCity(city: String) {
        cityAdapter.cities.filter {
            it.cityName != city
        }.map {
            it.isSelected = false
        }
        cityAdapter.cities.filter {
            it.cityName == city
        }.map {
            it.isSelected = true
        }
        viewModel.saveCityOfUserLocation(city)
    }

}