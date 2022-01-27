package com.andersen.feature_rooms_screen.presentation.di

import com.andersen.feature_rooms_screen.data.RoomsScreenApi
import com.andersen.feature_rooms_screen.data.RoomsScreenRepository
import com.andersen.feature_rooms_screen.domain.entity.InRoomsScreenRepository
import com.example.core_network.RequestMaker
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class RoomsScreenNetworkModule {

    @Provides
    fun getRoomsScreenApi(retrofit: Retrofit): RoomsScreenApi = retrofit.create(RoomsScreenApi::class.java)

    @Provides
    fun getRoomsScreenRepository(
        requestMaker: RequestMaker,
        roomsScreenApi: RoomsScreenApi
    ): InRoomsScreenRepository = RoomsScreenRepository(requestMaker, roomsScreenApi)
}