package com.meetingroom.feature_meet_now.presentation.di

import android.content.Context
import com.example.core_module.di.FeatureScope
import com.meetingroom.feature_meet_now.presentation.available_now_fragment.RoomsAvailableNowFragment
import com.meetingroom.feature_meet_now.presentation.di.viewmodel.ViewModelModule
import com.meetingroom.feature_meet_now.presentation.meet_now_fragment.MeetNowFragment
import dagger.BindsInstance
import dagger.Component

@FeatureScope
@Component(modules = [
    NetworkModule::class,
    ViewModelModule::class
])
interface MeetNowComponent {
    fun inject(meetNowFragment: MeetNowFragment)
    fun inject(roomsAvailableNowFragment: RoomsAvailableNowFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): MeetNowComponent
    }
}