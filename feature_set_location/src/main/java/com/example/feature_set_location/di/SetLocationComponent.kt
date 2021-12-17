package com.example.feature_set_location.di

import android.content.Context
import com.example.core_module.di.FeatureScope
import com.example.feature_set_location.LocationFragment
import com.example.feature_set_location.city_fragment.CityFragment
import com.example.feature_set_location.country_fragment.CountryFragment
import com.example.feature_set_location.di.view_model.ViewModelModule
import dagger.BindsInstance
import dagger.Component

@FeatureScope
@Component(modules = [LocationModule::class, ViewModelModule::class], dependencies = [SetLocationDeps::class])
interface SetLocationComponent {
    fun inject(cityFragment: CityFragment)
    fun inject(countryFragment: CountryFragment)
    fun inject(locationFragment: LocationFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context, setLocationDeps: SetLocationDeps): SetLocationComponent
    }
}