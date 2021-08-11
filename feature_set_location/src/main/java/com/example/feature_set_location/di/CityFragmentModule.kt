package com.example.feature_set_location.di

import androidx.lifecycle.ViewModelProvider
import com.example.feature_set_location.city_fragment.CityFragment
import com.example.feature_set_location.city_fragment.CityFragmentViewModel
import com.example.feature_set_location.city_fragment.CityViewModelFactory

import dagger.Module
import dagger.Provides

@Module
class CityFragmentModule(private val cityFragment: CityFragment) {

    @Provides
    fun provideCityViewModelFactory(): CityViewModelFactory = CityViewModelFactory()

    @Provides
    fun provideCityViewModel(cityViewModelFactory: CityViewModelFactory): CityFragmentViewModel =
        ViewModelProvider(cityFragment, cityViewModelFactory).get(CityFragmentViewModel::class.java)

}
