package com.andersen.feature_rooms_screen.presentation.di

import com.andersen.feature_rooms_screen.data.RoomsApi
import com.andersen.feature_rooms_screen.data.GagForRoomsScreen
import com.example.core_module.di.FeatureScope
import dagger.Binds
import dagger.Module

@Module
interface NetworkModule {

    @Binds
    @FeatureScope
    fun RoomsApi(
        gagForRoomsScreen: GagForRoomsScreen
    ): RoomsApi
}
