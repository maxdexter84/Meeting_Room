package com.meetingroom.andersen.feature_landing.di.time_for_notification_dialog

import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.ui.TimeForNotificationCustomDialog
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.ui.TimeForNotificationDialog
import dagger.Component

@Component(modules = [TimeForNotificationModule::class, SharedPreferencesModule::class])
@Screen
interface TimeForNotificationComponent {
    fun inject(timeForNotificationDialog: TimeForNotificationDialog)
    fun inject(timeForNotificationCustomDialog: TimeForNotificationCustomDialog)
}