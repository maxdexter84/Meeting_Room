package com.example.feature_set_location.di

import com.example.feature_set_location.city_fragment.CityFragment
import com.example.feature_set_location.city_fragment.CityFragmentViewModel
import com.example.feature_set_location.country_fragment.CountryFragment
import com.example.feature_set_location.country_fragment.CountryFragmentViewModel
import dagger.Component


@Component(modules = [CountryFragmentModule::class])
interface CountryComponent {

    fun provideCountryFragmentViewModel(): CountryFragmentViewModel

    fun inject(countryFragment: CountryFragment)


}

@Component(modules = [CityFragmentModule::class])
interface CityComponent {
    fun provideCityFragmentViewModel(): CityFragmentViewModel

    fun inject(cityFragment: CityFragment)
}