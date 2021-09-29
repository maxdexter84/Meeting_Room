package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.RoomAndTimePickerData

class TimeForNotificationViewModel(private val saveData: UserDataPrefHelperImpl) : ViewModel() {

    val userSelectedTime = MutableLiveData<String?>()

    fun saveUserTime(time: String) {
        saveData.saveTimeOfUserSelection(time)
        userSelectedTime.value = saveData.getTimeOfUserSelection()
    }

    fun getUserSelectedTime(): String? = saveData.getTimeOfUserSelection()

    fun changeSelected(roomsAndTime: ArrayList<RoomAndTimePickerData>, savedTime: String) {
        roomsAndTime.map {
            it.isSelected = it.roomAndTime == savedTime
        }
    }
}