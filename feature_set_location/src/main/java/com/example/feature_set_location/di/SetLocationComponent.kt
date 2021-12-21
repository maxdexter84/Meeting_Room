package com.example.feature_set_location.di

import android.content.Context
import com.example.core_module.di.FeatureScope
import com.example.feature_set_location.di.view_model.ViewModelModule
import com.example.feature_set_location.presentation.fragments.city_fragment.CityFragment
import com.example.feature_set_location.presentation.fragments.country_fragment.CountryFragment
import com.example.feature_set_location.presentation.fragments.location_fragment.LocationFragment
import dagger.BindsInstance
import dagger.Component

@FeatureScope
@Component(
    modules = [LocationModule::class, ViewModelModule::class, InteractorModule::class, RepositoryModule::class],
    dependencies = [SetLocationDeps::class]
)
interface SetLocationComponent {
    fun inject(cityFragment: CityFragment)
    fun inject(countryFragment: CountryFragment)
    fun inject(locationFragment: LocationFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            setLocationDeps: SetLocationDeps
        ): SetLocationComponent
    }
}