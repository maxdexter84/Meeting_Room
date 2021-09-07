package com.meetingroom.andersen.feature_landing.di.upcoming_events_fragment

import androidx.lifecycle.ViewModelProvider
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation.GagForUpcomingEvents
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation.UpcomingEventsFragmentViewModel
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation.UpcomingEventsFragmentViewModelFactory
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.ui.UpcomingEventsFragment
import dagger.Module
import dagger.Provides

@Module
class UpcomingEventsFragmentModule(private val upcomingEventsFragment: UpcomingEventsFragment) {

    @Provides
    @Screen
    fun provideGagForUpcomingEvents() = GagForUpcomingEvents()

    @Provides
    @Screen
    fun provideViewModelFactory(
        gagForUpcomingEvents: GagForUpcomingEvents
    ): UpcomingEventsFragmentViewModelFactory =
        UpcomingEventsFragmentViewModelFactory(gagForUpcomingEvents)

    @Provides
    @Screen
    fun provideViewModel(upcomingEventsFragmentViewModelFactory: UpcomingEventsFragmentViewModelFactory): UpcomingEventsFragmentViewModel {
        return ViewModelProvider(
            upcomingEventsFragment,
            upcomingEventsFragmentViewModelFactory
        ).get(UpcomingEventsFragmentViewModel::class.java)
    }

}