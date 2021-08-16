package com.example.feature_set_location.city_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.example.core_network.location_posts.GetAllAvailableCitiesRequest
import com.example.feature_set_location.databinding.CityFragmentBinding
import com.example.feature_set_location.di.CityFragmentModule
import com.example.feature_set_location.di.DaggerCityComponent
import javax.inject.Inject

class CityFragment : Fragment() {

    lateinit var binding: CityFragmentBinding
    private val cityAdapter = CityAdapter()
    lateinit var countryName: String

    @Inject
    lateinit var viewModel: CityFragmentViewModel

    @Inject
    lateinit var savedData: UserDataPrefHelperImpl


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerCityComponent.builder()
            .cityFragmentModule(CityFragmentModule(this))
            .sharedPreferencesModule(SharedPreferencesModule(requireContext()))
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        countryName = savedData.getCountryOfUserLocation()!!

        viewModel.tryToGetAllAvailableCities(
            GetAllAvailableCitiesRequest(
                countryName
            )
        )
        binding = CityFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewCityFragment.adapter = cityAdapter

        viewModel.requestResult.observe(viewLifecycleOwner, {
            cityAdapter.cities = it
        })
        binding.toolBarLocationFragment.arrowBackLocationFragment.setOnClickListener {
            findNavController().popBackStack()
        }
        requireActivity().supportFragmentManager.setFragmentResult(
            "requestKey", bundleOf(
                "bundleKey" to
                        cityAdapter.selectedCity
            )
        )
    }

}