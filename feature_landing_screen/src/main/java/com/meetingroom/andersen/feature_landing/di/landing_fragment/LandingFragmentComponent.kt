package com.meetingroom.andersen.feature_landing.di.landing_fragment

import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meetingroom.andersen.feature_landing.di.Screen
import dagger.Component
import com.meetingroom.andersen.feature_landing.landing_fragment.ui.UpcomingEventsFragment

@Component(modules = [LandingFragmentModule::class, SharedPreferencesModule::class])
@Screen
interface LandingFragmentComponent {
    fun inject(upcomingEventsFragment: UpcomingEventsFragment)
}