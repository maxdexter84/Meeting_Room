package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation

import androidx.lifecycle.ViewModel
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.RoomAndTimePickerData

class TimeForNotificationViewModel(private val saveData: UserDataPrefHelperImpl) : ViewModel() {

    fun saveUserTime(time: String) {
        saveData.saveTimeOfUserSelection(time)
    }

    fun getUserSelectedTime(): String? = saveData.getTimeOfUserSelection()

    fun changeSelected(roomsAndTime: ArrayList<RoomAndTimePickerData>, savedTime: String) {
        roomsAndTime.filter {
            it.roomAndTime != savedTime
        }.map {
            it.isSelected = false
        }
        roomsAndTime.filter {
            it.roomAndTime == savedTime
        }.map {
            it.isSelected = true
        }
    }
}