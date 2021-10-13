package com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment

import androidx.lifecycle.ViewModelProvider
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui.ModifyUpcomingEventFragment
import com.meetingroom.andersen.feature_landing.time_validation_dialog_manager.TimeValidationViewModel
import com.meetingroom.andersen.feature_landing.time_validation_dialog_manager.TimeValidationViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ModifyUpcomingEventFragmentModule(private val modifyUpcomingEventFragment: ModifyUpcomingEventFragment) {

    @Provides
    @Screen
    fun provideViewModelFactory(): TimeValidationViewModelFactory =
        TimeValidationViewModelFactory()

    @Provides
    @Screen
    fun provideViewModel(timeValidationViewModelFactory: TimeValidationViewModelFactory): TimeValidationViewModel {
        return ViewModelProvider(
            modifyUpcomingEventFragment,
            timeValidationViewModelFactory
        ).get(TimeValidationViewModel::class.java)
    }
}