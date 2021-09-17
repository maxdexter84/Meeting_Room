package com.meetingroom.andersen.feature_landing.di.upcoming_events_fragment

import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.ui.UpcomingEventsFragment
import dagger.Component

@Component(modules = [UpcomingEventsFragmentModule::class])
@Screen
interface UpcomingEventsFragmentComponent {
    fun inject(upcomingEventsFragment: UpcomingEventsFragment)
}