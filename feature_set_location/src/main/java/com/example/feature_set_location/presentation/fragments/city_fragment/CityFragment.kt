package com.example.feature_set_location.presentation.fragments.city_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.core_module.component_manager.XInjectionManager
import com.example.feature_set_location.R
import com.example.feature_set_location.databinding.CityFragmentBinding
import com.example.feature_set_location.di.SetLocationComponent
import com.example.feature_set_location.presentation.adapters.city_adapter.CityAdapter
import com.meeringroom.ui.view.base_classes.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityFragment : BaseFragment<CityFragmentBinding>(CityFragmentBinding::inflate) {

    private val cityAdapter =
        CityAdapter(onItemClick = {
            saveCity(it)
            lifecycleScope.launch {
                findNavController().navigate(R.id.locationFragment)
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
        val currentCity = CityFragmentArgs.fromBundle(requireArguments()).countryName
        viewModel.getCountryCityList(currentCity)
        viewModel.cityList.onEach {
            cityAdapter.cities = it
        }.launchIn(lifecycleScope)
        binding.recyclerViewCityFragment.adapter = cityAdapter
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
        viewModel.saveCity(city)
    }

    private fun injectDependencies() {
        XInjectionManager.findComponent<SetLocationComponent>()
            .inject(this)
    }


}