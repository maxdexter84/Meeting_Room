package com.meetingroom.andersen.feature_landing.presentation.di

import com.example.core_module.di.FeatureScope
import com.meetingroom.andersen.feature_landing.data.GagLanding
import com.meetingroom.andersen.feature_landing.data.RoomsApi
import dagger.Binds
import dagger.Module

@Module
interface LandingNetworkModule {

    @Binds
    @FeatureScope
    fun roomsApi(
        gagLanding: GagLanding
    ): RoomsApi
}
