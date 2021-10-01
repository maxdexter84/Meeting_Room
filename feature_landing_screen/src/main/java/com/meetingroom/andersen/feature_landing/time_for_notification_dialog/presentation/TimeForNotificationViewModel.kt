package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.RoomAndTimePickerData

class TimeForNotificationViewModel : ViewModel() {

    val userSelectedTime = MutableLiveData<String?>()

    fun saveUserTime(time: String) {
        userSelectedTime.value = time
    }

    fun changeSelected(roomsAndTime: ArrayList<RoomAndTimePickerData>, savedTime: String) {
        roomsAndTime.map {
            it.isSelected = it.roomAndTime == savedTime
        }
    }
}