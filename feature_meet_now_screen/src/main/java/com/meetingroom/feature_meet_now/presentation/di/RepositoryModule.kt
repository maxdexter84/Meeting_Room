package com.meetingroom.feature_meet_now.presentation.di

import com.example.core_network.RequestMaker
import com.meetingroom.feature_meet_now.data.AvailableRoomsApi
import com.meetingroom.feature_meet_now.data.AvailableRoomsRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideRepository(requestMaker: RequestMaker, roomsApi: AvailableRoomsApi) =
        AvailableRoomsRepository(requestMaker, roomsApi)
}