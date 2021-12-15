package com.andersen.feature_rooms_screen.presentation.di

import android.content.Context
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.presentation.NotificationHelper
import dagger.Module
import dagger.Provides

@Module
class NewEventModule() {


    @Provides
    fun provideNotificationHelper(context: Context) : NotificationHelper {
        return NotificationHelper(context)
    }
}