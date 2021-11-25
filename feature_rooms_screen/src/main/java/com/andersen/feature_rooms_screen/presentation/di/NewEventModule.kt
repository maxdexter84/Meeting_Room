package com.andersen.feature_rooms_screen.presentation.di

import android.content.Context
import com.andersen.feature_rooms_screen.presentation.new_event.dialog_time_for_notifications.NotificationHelper
import dagger.Module
import dagger.Provides

@Module
class NewEventModule(val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideNotificationHelper(context: Context) : NotificationHelper {
        return NotificationHelper(context)
    }
}