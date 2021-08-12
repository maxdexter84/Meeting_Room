package com.example.feature_set_location.di

import com.example.core_network.NetworkModule
import com.example.feature_set_location.city_fragment.CityFragment
import com.example.feature_set_location.city_fragment.CityFragmentViewModel
import dagger.Component

@Component(modules = [CityFragmentModule::class, NetworkModule::class])
interface CityComponent {
    fun provideCityFragmentViewModel(): CityFragmentViewModel

    fun inject(cityFragment: CityFragment)
}