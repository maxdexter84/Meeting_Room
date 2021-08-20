package com.example.feature_set_location.di

import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.example.core_network.NetworkModule
import com.example.feature_set_location.country_fragment.CountryFragment
import com.example.feature_set_location.country_fragment.CountryFragmentViewModel
import dagger.Component


@Component(modules = [CountryFragmentModule::class, NetworkModule::class, SharedPreferencesModule::class])
@Screen
interface CountryComponent {

    fun provideCountryFragmentViewModel(): CountryFragmentViewModel

    fun inject(countryFragment: CountryFragment)

}