package com.example.feature_set_location.city_fragment.di

import com.example.feature_set_location.city_fragment.CityFragment
import com.example.feature_set_location.city_fragment.CityFragmentViewModel
import dagger.Component

@Component(modules = [CityFragmentModule::class])
interface CityComponent {
    fun provideViewModel(): CityFragmentViewModel

    fun inject(cityFragment: CityFragment)
}