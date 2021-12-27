package com.meetingroom.andersen.feature_landing.presentation.di

import com.example.core_network.RequestMaker
import com.meetingroom.andersen.feature_landing.data.RoomsEventApi
import com.meetingroom.andersen.feature_landing.data.RoomsEventRepository
import com.meetingroom.andersen.feature_landing.domain.entity.IRoomsEventRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class LandingNetworkModule {

    @Provides
    fun getRoomsEventApi(retrofit: Retrofit): RoomsEventApi = retrofit.create(RoomsEventApi::class.java)

    @Provides
    fun getRoomsEventRepository(
        requestMaker: RequestMaker,
        roomsEventApi: RoomsEventApi
    ): IRoomsEventRepository = RoomsEventRepository(requestMaker, roomsEventApi)
}