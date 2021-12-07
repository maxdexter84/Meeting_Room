package com.example.feature_set_location.city_fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.feature_set_location.LocationFragment
import com.example.feature_set_location.R
import com.example.feature_set_location.databinding.CityFragmentBinding
import com.example.feature_set_location.di.SetLocationComponent
import com.meeringroom.ui.view.base_classes.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.vponomarenko.injectionmanager.x.XInjectionManager
import javax.inject.Inject

class CityFragment : BaseFragment<CityFragmentBinding>(CityFragmentBinding::inflate) {

    private val cityAdapter =
        CityAdapter(onItemClick = {
            saveCity(it)
            lifecycleScope.launch {
                delay(DELAY_AFTER_CLICK_CITY)
                findNavController().navigate(R.id.locationFragment,  bundleOf(LocationFragment.CITY_KEY to it))
            }
        })

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CityFragmentViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
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

    private fun injectDependencies() {
        XInjectionManager.findComponent<SetLocationComponent>()
            .inject(this)
    }

    companion object{
        const val DELAY_AFTER_CLICK_CITY = 3L
    }
}