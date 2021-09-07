package com.meetingroom.andersen.feature_landing.di.history_of_events_fragment

import com.example.core_network.NetworkModule
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.history_of_events_fragment.presentation.HistoryOfEventsFragmentViewModel
import com.meetingroom.andersen.feature_landing.history_of_events_fragment.ui.HistoryOfEventsFragment
import dagger.Component

@Component(modules = [HistoryOfEventsFragmentModule::class, NetworkModule::class])
@Screen
interface HistoryOfEventsFragmentComponent {
    fun provideViewModel(): HistoryOfEventsFragmentViewModel

    fun inject(historyOfEventsFragment: HistoryOfEventsFragment)
}