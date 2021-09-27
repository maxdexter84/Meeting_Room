package com.meetingroom.andersen.feature_landing.di.time_for_notification_dialog

import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation.TimeForNotificationViewModelFactory
import dagger.Provides

class TimeForNotificationModule {

    @Provides
    @Screen
    fun provideViewModelFactory(saveData: UserDataPrefHelperImpl): TimeForNotificationViewModelFactory =
        TimeForNotificationViewModelFactory(saveData)
}