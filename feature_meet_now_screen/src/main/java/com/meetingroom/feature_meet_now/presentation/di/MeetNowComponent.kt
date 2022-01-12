package com.meetingroom.feature_meet_now.presentation.di

import android.content.Context
import com.example.core_module.di.FeatureScope
import com.meetingroom.feature_meet_now.presentation.available_now_fragment.RoomsAvailableNowFragment
import com.meetingroom.feature_meet_now.presentation.available_soon_fragment.RoomsAvailableSoonFragment
import com.meetingroom.feature_meet_now.presentation.di.viewmodel.ViewModelModule
import com.meetingroom.feature_meet_now.presentation.meet_now_fragment.MeetNowFragment
import dagger.BindsInstance
import dagger.Component

@FeatureScope
@Component(
    modules = [
        NetworkModule::class,
        RepositoryModule::class,
        ViewModelModule::class
    ], dependencies = [MeetNowDeps::class]
)
interface MeetNowComponent {
    fun inject(meetNowFragment: MeetNowFragment)
    fun inject(roomsAvailableNowFragment: RoomsAvailableNowFragment)
    fun inject(roomsAvailableSoonFragment: RoomsAvailableSoonFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context, meetNowDeps: MeetNowDeps): MeetNowComponent
    }
}