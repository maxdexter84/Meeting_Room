package com.example.feature_set_location.di.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.feature_set_location.city_fragment.CityFragmentViewModel
import com.example.feature_set_location.country_fragment.CountryFragmentViewModel
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
    fun bindCountryFragmentViewModel(roomsEventViewModel: CountryFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CityFragmentViewModel::class)
    fun bindCityFragmentViewModel(roomsEventViewModel: CityFragmentViewModel): ViewModel
}
