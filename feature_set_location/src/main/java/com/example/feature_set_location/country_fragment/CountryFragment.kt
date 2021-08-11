package com.example.feature_set_location.country_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.example.feature_set_location.SharedViewModel
import com.example.feature_set_location.country_fragment.di.CountryFragmentModule
import com.example.feature_set_location.country_fragment.di.DaggerCountryComponent
import com.example.feature_set_location.databinding.CountryFragmentBinding
import javax.inject.Inject


class CountryFragment : Fragment() {

    lateinit var binding: CountryFragmentBinding
    private val countryAdapter = CountryAdapter()
    lateinit var sharedViewModel: SharedViewModel

    @Inject
    lateinit var viewModel: CountryFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerCountryComponent.builder()
            .countryFragmentModule(CountryFragmentModule(this))
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CountryFragmentBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

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
            val request = NavDeepLinkRequest.Builder
                .fromUri("android-app://com.meetingroom.app/cityFragment".toUri())
                .build()
            findNavController().navigate(request)
            sharedViewModel.select(it)
        }
    }
}