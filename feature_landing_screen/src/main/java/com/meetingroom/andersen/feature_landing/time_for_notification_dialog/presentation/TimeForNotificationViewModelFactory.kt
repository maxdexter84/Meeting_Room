package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl

@Suppress("UNCHECKED_CAST")
class TimeForNotificationViewModelFactory(
    private val saveData: UserDataPrefHelperImpl
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimeForNotificationViewModel::class.java)) {
            return TimeForNotificationViewModel(
                saveData,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}