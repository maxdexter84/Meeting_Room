package com.example.feature_set_location.di.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.feature_set_location.presentation.fragments.city_fragment.CityFragmentViewModel
import com.example.feature_set_location.presentation.fragments.country_fragment.CountryFragmentViewModel
import com.example.feature_set_location.presentation.fragments.location_fragment.LocationFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CountryFragmentViewModel::class)
    fun bindCountryFragmentViewModel(countryViewModel: CountryFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CityFragmentViewModel::class)
    fun bindCityFragmentViewModel(cityViewModel: CityFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationFragmentViewModel::class)
    fun bindLocationFragmentViewModel(locationViewModel: LocationFragmentViewModel): ViewModel
}
