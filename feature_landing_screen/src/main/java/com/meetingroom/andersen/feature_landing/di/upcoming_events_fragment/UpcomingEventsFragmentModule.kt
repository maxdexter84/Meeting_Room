package com.meetingroom.andersen.feature_landing.di.upcoming_events_fragment

import androidx.lifecycle.ViewModelProvider
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.example.core_module.user_logout.LogOutHelper
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation.GagForUpcomingEvents
import dagger.Module
import dagger.Provides
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.ui.UpcomingEventsFragment
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation.UpcomingEventsFragmentViewModel
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation.UpcomingEventsFragmentViewModelFactory

@Module
class UpcomingEventsFragmentModule(private val upcomingEventsFragment: UpcomingEventsFragment) {

    @Provides
    @Screen
    fun provideGagForUpcomingEvents() = GagForUpcomingEvents()

    @Provides
    @Screen
    fun provideLogOutHelper(saveData: UserDataPrefHelperImpl): LogOutHelper {
        return LogOutHelper(saveData)
    }

    @Provides
    @Screen
    fun provideViewModelFactory(
        logOutHelper: LogOutHelper,
        gagForUpcomingEvents: GagForUpcomingEvents
    ): UpcomingEventsFragmentViewModelFactory =
        UpcomingEventsFragmentViewModelFactory(logOutHelper, gagForUpcomingEvents)

    @Provides
    @Screen
    fun provideViewModel(upcomingEventsFragmentViewModelFactory: UpcomingEventsFragmentViewModelFactory): UpcomingEventsFragmentViewModel {
        return ViewModelProvider(
            upcomingEventsFragment,
            upcomingEventsFragmentViewModelFactory
        ).get(UpcomingEventsFragmentViewModel::class.java)
    }

}