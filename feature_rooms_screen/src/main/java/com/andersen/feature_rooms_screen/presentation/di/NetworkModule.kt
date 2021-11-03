package com.andersen.feature_rooms_screen.presentation.di

import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.GagForRoomEvents
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.GagForRooms
import com.example.core_module.di.FeatureScope
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {

    @Provides
    @FeatureScope
    fun provideGagForRoomEvents(): GagForRoomEvents = GagForRoomEvents()

    @Provides
    @FeatureScope
    fun provideGagForRooms(): GagForRooms = GagForRooms()

}
