package com.example.feature_set_location.country_fragment.di

import androidx.lifecycle.ViewModelProvider
import com.example.feature_set_location.country_fragment.CountryFragment
import com.example.feature_set_location.country_fragment.CountryFragmentViewModel
import com.example.feature_set_location.country_fragment.CountryViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class CountryFragmentModule(private val countryFragment: CountryFragment) {

    @Provides
    fun provideViewModelFactory(): CountryViewModelFactory = CountryViewModelFactory()

    @Provides
    fun provideLoginViewModel(countryViewModelFactory: CountryViewModelFactory): CountryFragmentViewModel =
        ViewModelProvider(countryFragment, countryViewModelFactory).get(CountryFragmentViewModel::class.java)
}