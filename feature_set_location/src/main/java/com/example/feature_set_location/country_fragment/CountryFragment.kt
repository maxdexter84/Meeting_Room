package com.example.feature_set_location.country_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.example.feature_set_location.R
import com.example.feature_set_location.databinding.CountryFragmentBinding
import com.example.feature_set_location.di.CountryFragmentModule
import com.example.feature_set_location.di.DaggerCountryComponent
import javax.inject.Inject


class CountryFragment : Fragment() {

    lateinit var binding: CountryFragmentBinding
    private val countryAdapter = CountryAdapter()

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CountryFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewCountryFragment.adapter = countryAdapter

        viewModel.requestResult.observe(viewLifecycleOwner, {
            countryAdapter.countries = it
        })
        viewModel.tryToGetAllAvailableCountries()

        binding.toolBarLocationFragment.arrowBackLocationFragment.setOnClickListener {
            findNavController().popBackStack()
        }

        countryAdapter.onItemClick = {
            findNavController().navigate(R.id.action_countryFragment_to_cityFragment)
            viewModel.saveData.saveCountryOfUserLocation(it)
        }
    }
}