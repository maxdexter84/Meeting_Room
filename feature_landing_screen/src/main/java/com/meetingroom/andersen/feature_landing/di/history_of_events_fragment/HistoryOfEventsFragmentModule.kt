package com.meetingroom.andersen.feature_landing.di.history_of_events_fragment

import com.example.core_network.RequestMaker
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.history_of_events_fragment.presentation.HistoryOfEventsViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class HistoryOfEventsFragmentModule {

    @Provides
    @Screen
    fun provideViewModelFactory(
        requestMaker: RequestMaker
    ): HistoryOfEventsViewModelFactory = HistoryOfEventsViewModelFactory(requestMaker)
}