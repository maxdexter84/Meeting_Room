package com.meetingroom.andersen.feature_landing.presentation.di

import android.content.Context
import com.example.core_module.di.FeatureScope
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.presentation.NotificationHelper
import com.meetingroom.andersen.feature_landing.presentation.modify_upcoming_fragment.ModifyUpcomingEventFragment
import dagger.Module
import dagger.Provides

@Module
class ModifyUpcomingEventFragmentModule() {

    @Provides
    @FeatureScope
    fun provideNotificationHelper(context: Context) = NotificationHelper(context)
}