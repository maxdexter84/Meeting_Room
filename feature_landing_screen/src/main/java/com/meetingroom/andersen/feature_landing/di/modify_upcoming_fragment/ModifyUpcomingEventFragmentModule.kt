package com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.core_module.event_time_validation.TimeValidationDialogManager
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.presentation.NotificationHelper
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.data.GagForRooms
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.ModifyUpcomingEventFragment
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.ModifyUpcomingEventViewModel
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.ModifyUpcomingEventViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ModifyUpcomingEventFragmentModule(private val modifyUpcomingEventFragment: ModifyUpcomingEventFragment) {

    @Provides
    @Screen
    fun provideViewModelFactory(
        gagForRooms: GagForRooms,
        dialogManager: TimeValidationDialogManager
    ): ModifyUpcomingEventViewModelFactory = ModifyUpcomingEventViewModelFactory(gagForRooms, dialogManager)

    @Provides
    @Screen
    fun provideViewModel(modifyUpcomingEventViewModelFactory: ModifyUpcomingEventViewModelFactory): ModifyUpcomingEventViewModel {
        return ViewModelProvider(
            modifyUpcomingEventFragment,
            modifyUpcomingEventViewModelFactory
        ).get(ModifyUpcomingEventViewModel::class.java)
    }

    @Provides
    @Screen
    fun provideNotificationHelper(context: Context) = NotificationHelper(context)
}