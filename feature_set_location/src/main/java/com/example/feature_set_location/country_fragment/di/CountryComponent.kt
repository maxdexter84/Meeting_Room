package com.example.feature_set_location.country_fragment.di

import com.example.feature_set_location.country_fragment.CountryFragment
import com.example.feature_set_location.country_fragment.CountryFragmentViewModel
import dagger.Component

@Component(modules = [CountryFragmentModule::class])
interface CountryComponent {

    fun provideViewModel(): CountryFragmentViewModel

    fun inject(countryFragment: CountryFragment)
}