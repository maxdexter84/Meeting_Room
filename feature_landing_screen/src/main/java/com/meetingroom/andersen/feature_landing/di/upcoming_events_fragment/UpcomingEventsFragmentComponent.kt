package com.meetingroom.andersen.feature_landing.di.upcoming_events_fragment

import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meetingroom.andersen.feature_landing.di.Screen
import dagger.Component
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.ui.UpcomingEventsFragment

@Component(modules = [UpcomingEventsFragmentModule::class, SharedPreferencesModule::class])
@Screen
interface UpcomingEventsFragmentComponent {
    fun inject(upcomingEventsFragment: UpcomingEventsFragment)
}