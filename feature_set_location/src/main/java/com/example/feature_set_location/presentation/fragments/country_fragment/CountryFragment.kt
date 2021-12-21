package com.example.feature_set_location.presentation.fragments.country_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.core_module.component_manager.XInjectionManager
import com.example.feature_set_location.databinding.CountryFragmentBinding
import com.example.feature_set_location.di.SetLocationComponent
import com.example.feature_set_location.presentation.adapters.country_adapter.CountryAdapter
import com.meeringroom.ui.view.base_classes.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        viewModel.countryList.onEach {
            countryAdapter.countries = it
        }.launchIn(lifecycleScope)
        binding.toolBarLocationFragment.arrowBackLocationFragment.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun moveToCitiesAndSaveCountryName(countryName: String) {
        findNavController().navigate(
            CountryFragmentDirections.actionCountryFragmentToCityFragment(countryName)
        )
    }

    private fun injectDependencies() {
        XInjectionManager.findComponent<SetLocationComponent>()
            .inject(this)
    }
}