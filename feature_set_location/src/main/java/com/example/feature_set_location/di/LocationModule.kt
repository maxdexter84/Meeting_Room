package com.example.feature_set_location.di

import com.example.core_network.GagForInternetsRequests
import com.example.core_network.location_interfaces.LocationInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class LocationModule {

    @Provides
    fun getLocationInterface(): LocationInterface {
        return GagForInternetsRequests()
    }
}