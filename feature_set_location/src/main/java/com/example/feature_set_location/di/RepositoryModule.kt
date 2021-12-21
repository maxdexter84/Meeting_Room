package com.example.feature_set_location.di

import com.example.core_network.RequestMaker
import com.example.feature_set_location.data.remote.location_api.LocationApi
import com.example.feature_set_location.data.remote.repository.LocationRepositoryImpl
import com.example.feature_set_location.domain.repository.LocationRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun bindsLocationRepository(
        requestMaker: RequestMaker,
        api: LocationApi,
    ): LocationRepository = LocationRepositoryImpl(requestMaker, api)
}