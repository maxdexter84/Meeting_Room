package com.meetingroom.feature_meet_now.presentation.di

import com.example.core_module.di.FeatureScope
import com.meetingroom.feature_meet_now.data.GagMeetNow
import com.meetingroom.feature_meet_now.data.RoomsApi
import dagger.Binds
import dagger.Module

@Module
interface NetworkModule {
    @Binds
    @FeatureScope
    fun RoomsApi(
        gagMeetNow: GagMeetNow
    ): RoomsApi
}