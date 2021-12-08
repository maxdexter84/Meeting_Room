package com.example.feature_set_location.country_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.core_module.component_manager.XInjectionManager
import com.example.feature_set_location.R
import com.example.feature_set_location.databinding.CountryFragmentBinding
import com.example.feature_set_location.di.SetLocationComponent
import com.meeringroom.ui.view.base_classes.BaseFragment
import javax.inject.Inject

class CountryFragment : BaseFragment<CountryFragmentBinding>(CountryFragmentBinding::inflate) {

    private val countryAdapter = CountryAdapter(onItemClick = {
        moveToCitiesAndSaveCountryName(it)
    })

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CountryFragmentViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewCountryFragment.adapter = countryAdapter

        viewModel.requestResult.observe(viewLifecycleOwner, {
            countryAdapter.countries = it
        })

        binding.toolBarLocationFragment.arrowBackLocationFragment.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun moveToCitiesAndSaveCountryName(countryName: String) {
        findNavController().navigate(R.id.action_countryFragment_to_cityFragment)
        viewModel.saveCountryOfUserLocation(countryName)
    }

    private fun injectDependencies() {
        XInjectionManager.findComponent<SetLocationComponent>()
            .inject(this)
    }
}