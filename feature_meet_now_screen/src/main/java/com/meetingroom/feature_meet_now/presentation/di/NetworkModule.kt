package com.meetingroom.feature_meet_now.presentation.di

import com.meetingroom.feature_meet_now.data.AvailableRoomsApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class NetworkModule {

    @Provides
    fun provideRoomsApi(
        retrofit: Retrofit
    ): AvailableRoomsApi = retrofit.create(AvailableRoomsApi::class.java)
}