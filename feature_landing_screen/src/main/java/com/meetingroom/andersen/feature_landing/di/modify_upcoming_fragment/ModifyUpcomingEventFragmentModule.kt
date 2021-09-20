package com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment

import androidx.lifecycle.ViewModelProvider
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.ModifyUpcomingEventViewModel
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.ModifyUpcomingEventViewModelFactory
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui.ModifyUpcomingEventFragment
import dagger.Module
import dagger.Provides

@Module
class ModifyUpcomingEventFragmentModule(private val modifyUpcomingEventFragment: ModifyUpcomingEventFragment) {

    @Provides
    @Screen
    fun provideViewModelFactory(): ModifyUpcomingEventViewModelFactory =
        ModifyUpcomingEventViewModelFactory()

    @Provides
    @Screen
    fun provideViewModel(modifyUpcomingEventViewModelFactory: ModifyUpcomingEventViewModelFactory): ModifyUpcomingEventViewModel {
        return ViewModelProvider(
            modifyUpcomingEventFragment,
            modifyUpcomingEventViewModelFactory
        ).get(ModifyUpcomingEventViewModel::class.java)
    }

}