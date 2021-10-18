package com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment

import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui.ModifyUpcomingEventFragment
import dagger.Component

@Component(modules = [ModifyUpcomingEventFragmentModule::class])
@Screen
interface ModifyUpcomingEventFragmentComponent {
    fun inject(modifyUpcomingEventFragment: ModifyUpcomingEventFragment)
}