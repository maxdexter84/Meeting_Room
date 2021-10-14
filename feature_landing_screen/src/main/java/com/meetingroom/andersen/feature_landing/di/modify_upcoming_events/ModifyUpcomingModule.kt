package com.meetingroom.andersen.feature_landing.di.modify_upcoming_events

import android.content.Context
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.NotificationHelper
import dagger.Module
import dagger.Provides

@Module
class ModifyUpcomingModule {

    @Provides
    @Screen
    fun provideNotificationHelper(context: Context) : NotificationHelper {
        return NotificationHelper(context)
    }
}