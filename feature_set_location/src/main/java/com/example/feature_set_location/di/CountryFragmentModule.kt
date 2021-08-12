package com.example.feature_set_location.di

import androidx.lifecycle.ViewModelProvider
import com.example.core_network.RequestMaker
import com.example.feature_set_location.country_fragment.CountryFragment
import com.example.feature_set_location.country_fragment.CountryFragmentViewModel
import com.example.feature_set_location.country_fragment.CountryViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class CountryFragmentModule(private val countryFragment: CountryFragment) {

    @Provides
    @Screen
    fun provideCountryViewModelFactory(requestMaker: RequestMaker): CountryViewModelFactory =
        CountryViewModelFactory(requestMaker)

    @Provides
    @Screen
    fun provideCountryViewModel(countryViewModelFactory: CountryViewModelFactory): CountryFragmentViewModel =
        ViewModelProvider(
            countryFragment,
            countryViewModelFactory
        ).get(CountryFragmentViewModel::class.java)
}