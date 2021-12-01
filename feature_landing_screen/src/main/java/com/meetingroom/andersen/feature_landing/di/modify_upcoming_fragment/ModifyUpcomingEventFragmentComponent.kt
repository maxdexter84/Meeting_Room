package com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment

import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.ModifyUpcomingEventFragment

import dagger.Component

@Component(modules = [ModifyUpcomingEventFragmentModule::class, SharedPreferencesModule::class])
@Screen
interface ModifyUpcomingEventFragmentComponent {
    fun inject(modifyUpcomingEventFragment: ModifyUpcomingEventFragment)
}