package com.example.feature_set_location.di

import com.example.feature_set_location.data.remote.location_api.LocationApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class LocationModule {

    @Provides
    fun getLocationApi(retrofit: Retrofit): LocationApi = retrofit.create(LocationApi::class.java)
}