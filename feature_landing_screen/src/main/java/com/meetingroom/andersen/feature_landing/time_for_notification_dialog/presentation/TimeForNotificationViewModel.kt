package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation

import androidx.lifecycle.ViewModel
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl

class TimeForNotificationViewModel(private val saveData: UserDataPrefHelperImpl) : ViewModel() {

    fun saveUserTime(time: String) {
        saveData.saveTimeOfUserSelection(time)
    }

    fun getUserSelectedTime(): String? = saveData.getTimeOfUserSelection()

    fun sf() = "Shared"
}