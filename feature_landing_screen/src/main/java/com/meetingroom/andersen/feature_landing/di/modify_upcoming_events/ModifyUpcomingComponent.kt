package com.meetingroom.andersen.feature_landing.di.modify_upcoming_events

import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui.ModifyUpcomingEventFragment
import dagger.Component

@Component(modules = [ModifyUpcomingModule::class, SharedPreferencesModule::class])
@Screen
interface ModifyUpcomingComponent {
    fun inject(modifyUpcomingEventFragment: ModifyUpcomingEventFragment)
}