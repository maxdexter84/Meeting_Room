package com.example.feature_set_location.country_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.example.feature_set_location.R
import com.example.feature_set_location.databinding.CountryFragmentBinding
import com.example.feature_set_location.di.CountryFragmentModule
import com.example.feature_set_location.di.DaggerCountryComponent
import com.meeringroom.ui.view.base_fragment.BaseFragment
import javax.inject.Inject


class CountryFragment : BaseFragment<CountryFragmentBinding>(CountryFragmentBinding::inflate) {

    private val countryAdapter = CountryAdapter(onItemClick = {
        moveToCitiesAndSaveCountryName(it)
    })

    @Inject
    lateinit var viewModel: CountryFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerCountryComponent.builder()
            .countryFragmentModule(CountryFragmentModule(this))
            .sharedPreferencesModule(SharedPreferencesModule(requireContext()))
            .build()
            .inject(this)
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
}